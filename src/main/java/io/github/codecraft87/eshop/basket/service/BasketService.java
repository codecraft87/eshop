package io.github.codecraft87.eshop.basket.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.basket.dto.BasketItemRequest;
import io.github.codecraft87.eshop.basket.dto.BasketRequest;
import io.github.codecraft87.eshop.basket.dto.BasketResponse;
import io.github.codecraft87.eshop.basket.entity.Basket;
import io.github.codecraft87.eshop.basket.entity.BasketItem;
import io.github.codecraft87.eshop.basket.enums.BasketStatus;
import io.github.codecraft87.eshop.basket.event.BasketCheckedOutEvent;
import io.github.codecraft87.eshop.basket.event.BasketItemEvent;
import io.github.codecraft87.eshop.basket.mapper.BasketMapper;
import io.github.codecraft87.eshop.basket.outbox.BasketOutboxMessage;
import io.github.codecraft87.eshop.basket.outbox.BasketOutboxService;
import io.github.codecraft87.eshop.basket.outbox.OutboxEventStatus;
import io.github.codecraft87.eshop.basket.outbox.OutboxEventType;
import io.github.codecraft87.eshop.basket.repository.BasketRepository;
import io.github.codecraft87.eshop.catalog.entity.Product;
import io.github.codecraft87.eshop.catalog.service.CatalogModuleService;
import io.github.codecraft87.eshop.exceptions.BasketNotFoundException;
import io.github.codecraft87.eshop.notification.service.NotificationModuleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Slf4j
@Service
public class BasketService implements BasketModuleService {

  private final BasketRepository basketRepository;
  private final CatalogModuleService catalogService;
  private final NotificationModuleService notificationService;
  private final BasketOutboxService outboxService;
  private final ObjectMapper objectMapper;

  public Long saveBasket(BasketRequest basketRequest) {
    Basket activeBasket = getActiveBasketForUser(basketRequest);

    if (activeBasket == null) {
      activeBasket = new Basket();
      addItemsToBasket(activeBasket, basketRequest);
    } else {
      updateBasket(activeBasket, basketRequest);
    }
    notificationService.basketUpdated(activeBasket.getId());
    return activeBasket.getId();
  }

  public List<BasketResponse> getBasketDetails(Long userId) {

    Basket basket = getActiveBasketForUser(new BasketRequest(userId));

    List<BasketResponse> basketDetails = null;

    if (basket != null) {
      basketDetails = basket.getItems().stream().map(BasketMapper::toBasketItemResponse).toList();

    } else {
      throw new BasketNotFoundException(userId);
    }
    return basketDetails;
  }

  @Transactional
  public void checkout(BasketRequest itemRequest) {
    log.info("checkout basket v1.");
    Basket basket = getActiveBasketForUser(itemRequest);

    if (basket == null || basket.getItems().isEmpty()) {
      throw new IllegalStateException("Basket is empty");
    }
    basket.setStatus(BasketStatus.CHECKOUT_IN_PROGRESS);
    saveBasket(basket);
    BasketOutboxMessage basketOutboxEventEntity = buildBasketCheckedOutOutboxEvent(basket);
    outboxService.saveBasketOutboxEvent(basketOutboxEventEntity);
  }

  private BasketOutboxMessage buildBasketCheckedOutOutboxEvent(Basket basket) {
    log.info("Build basket checkout event entity");
    BasketOutboxMessage entity = new BasketOutboxMessage();
    entity.setEventId(UUID.randomUUID());
    entity.setEventType(OutboxEventType.BASKET_CHECKED_OUT);
    entity.setStatus(OutboxEventStatus.NEW);
    entity.setRetryCount(0);
    entity.setCreatedAt(Instant.now());
    BasketCheckedOutEvent checkedOutEvent = getBasketCheckedOutEvent(basket, entity);
    try {
      entity.setPayload(objectMapper.writeValueAsString(checkedOutEvent));
    } catch (JacksonException ex) {
      log.error("Failed to serialize BasketCheckedOutEvent", ex);

      throw new RuntimeException("Failed to serialize BasketCheckedOutEvent", ex);
    }
    return entity;
  }

  private BasketCheckedOutEvent getBasketCheckedOutEvent(
      Basket basket, BasketOutboxMessage outboxEvent) {
    log.info("return json payload ");
    return new BasketCheckedOutEvent(
        outboxEvent.getEventId().toString(),
        basket.getId(),
        basket.getUserId(),
        basket.getItems().stream().map(this::gBasketItemEvent).toList());
  }

  private Basket getActiveBasketForUser(BasketRequest basketRequest) {
    Basket basket =
        basketRepository.findByUserIdAndStatus(basketRequest.getUserId(), BasketStatus.ACTIVE);
    return basket;
  }

  private void addItemsToBasket(Basket basket, BasketRequest basketRequest) {

    List<BasketItem> basketItems = getBasketItemsFromRequest(basket, basketRequest);

    basket.setItems(basketItems);
    basket.setUserId(basketRequest.getUserId());
    basket.setStatus(BasketStatus.ACTIVE);
    basket.setCreatedAt(Instant.now());

    saveBasket(basket);
  }

  private List<BasketItem> getBasketItemsFromRequest(Basket basket, BasketRequest basketRequest) {
    List<BasketItem> basketItems = new ArrayList<BasketItem>();

    for (BasketItemRequest item : basketRequest.getItems()) {
      Product product = findProductById(item.getProductId());
      BasketItem basketItem = new BasketItem();
      basketItem.setBasket(basket);
      basketItem.setProduct(product);
      basketItem.setQuantity(item.getQuantity());
      basketItems.add(basketItem);
    }
    return basketItems;
  }

  private void updateBasket(Basket basket, BasketRequest basketRequest) {

    for (BasketItemRequest itemDto : basketRequest.getItems()) {
      if (itemDto.getQuantity() <= 0) {

        removeProductFromBasket(basket, itemDto);

        continue;
      }
      Optional<BasketItem> optionalbasketItem = basket.findItemByProductId(itemDto.getProductId());
      if (optionalbasketItem.isPresent()) {
        BasketItem basketItem = optionalbasketItem.get();
        if (itemDto.getQuantity() > 0 && !basketItem.getQuantity().equals(itemDto.getQuantity())) {
          basketItem.setQuantity(itemDto.getQuantity());
        }
      } else {
        BasketItem basketItem = new BasketItem();
        basketItem.setBasket(basket);
        Product product = findProductById(itemDto.getProductId());
        basketItem.setProduct(product);
        basketItem.setQuantity(itemDto.getQuantity());
        basket.getItems().add(basketItem);
      }
    }
    syncBasket(basket, basketRequest);
    if (basket.getItems().isEmpty()) {
      basket.setStatus(BasketStatus.ABANDONED);
    }
    saveBasket(basket);
  }

  private void syncBasket(Basket basket, BasketRequest basketRequest) {
    basket
        .getItems()
        .removeIf(
            basketItem ->
                basketRequest.getItems().stream()
                    .noneMatch(
                        item -> item.getProductId().equals(basketItem.getProduct().getId())));
  }

  private void removeProductFromBasket(Basket basket, BasketItemRequest itemDto) {
    basket.getItems().removeIf(item -> item.getProduct().getId().equals(itemDto.getProductId()));
  }

  private Product findProductById(Long productId) {
    Product product = catalogService.getProduct(productId);
    return product;
  }

  private void saveBasket(Basket basket) {
    basket.setUpdatedAt(Instant.now());
    basketRepository.save(basket);
  }

  public BasketItemEvent gBasketItemEvent(BasketItem basketItem) {
    return new BasketItemEvent(basketItem.getProduct().getId(), basketItem.getQuantity());
  }

  @Transactional
  public void updateBasketForOrder(Long basketId) {
    Basket basket =
        basketRepository
            .findById(basketId)
            .orElseThrow(() -> new BasketNotFoundException(basketId));
    if (basket.getStatus() == BasketStatus.CHECKOUT_IN_PROGRESS) {
      basket.setStatus(BasketStatus.CHECKED_OUT);
      basket.getItems().clear();
      saveBasket(basket);
      log.info("Basket checkout flow completed for async communication");
    } else {
      throw new IllegalStateException(
          "On order ceated, Basket is invalid state " + basket.getStatus());
    }
  }
}

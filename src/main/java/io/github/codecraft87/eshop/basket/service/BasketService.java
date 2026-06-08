package io.github.codecraft87.eshop.basket.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.basket.dto.BasketItemRequest;
import io.github.codecraft87.eshop.basket.dto.BasketRequest;
import io.github.codecraft87.eshop.basket.dto.BasketResponse;
import io.github.codecraft87.eshop.basket.entity.Basket;
import io.github.codecraft87.eshop.basket.entity.BasketItem;
import io.github.codecraft87.eshop.basket.repository.BasketRepository;
import io.github.codecraft87.eshop.catalog.entity.Product;
import io.github.codecraft87.eshop.catalog.service.CatalogModuleService;
import io.github.codecraft87.eshop.common.enums.BasketStatus;
import io.github.codecraft87.eshop.common.enums.OrderStatus;
import io.github.codecraft87.eshop.exceptions.BasketNotFoundException;
import io.github.codecraft87.eshop.notification.service.NotificationModuleService;
import io.github.codecraft87.eshop.order.dto.OrderItemRequest;
import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.service.OrderModuleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BasketService implements BasketModuleService {

    private final BasketRepository basketRepository;
    private final CatalogModuleService catalogService;
    private final OrderModuleService orderService;
    private final NotificationModuleService notificationService;

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
        
        if(basket!=null) {
            basketDetails = basket.getItems()
                                .stream()
                                .map(BasketResponse::getBasketItemResponse)
                                .toList();
        }else {
            throw new BasketNotFoundException(userId);
        }
        return basketDetails;
    }

    @Transactional
    public void checkout(BasketRequest itemRequest) {
        Basket basket = getActiveBasketForUser(itemRequest);
        
        if (basket == null ||
                basket.getItems().isEmpty()) {
            throw new IllegalStateException("Basket is empty");
        }
        placeAnOrder(basket);
        basket.getItems().clear();
        basket.setStatus(BasketStatus.CHECKED_OUT);
        saveBasket(basket);
        notificationService.basketCheckout(basket.getId());
    }

    private Basket getActiveBasketForUser(BasketRequest basketRequest) {
        Basket basket = basketRepository.findByUserIdAndStatus(
                        basketRequest.getUserId(), BasketStatus.ACTIVE);
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

    private List<BasketItem> getBasketItemsFromRequest(Basket basket, 
                                            BasketRequest basketRequest) {
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

    private void updateBasket(
            Basket basket,
            BasketRequest basketRequest) {

        for (BasketItemRequest itemDto : basketRequest.getItems()) {
            if (itemDto.getQuantity() <= 0) {

                removeProductFromBasket(basket, itemDto);

                continue;
            }
            Optional<BasketItem> optionalbasketItem = basket.findItemByProductId(
                    itemDto.getProductId());
            if (optionalbasketItem.isPresent()) {
                BasketItem basketItem = optionalbasketItem.get();
                if (itemDto.getQuantity() > 0 &&
                        !basketItem.getQuantity().equals(itemDto.getQuantity())) {
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
        basket.getItems().removeIf(basketItem -> basketRequest.getItems().stream()
                .noneMatch(item -> item.getProductId().equals(basketItem.getProduct().getId())));
    }

    private void removeProductFromBasket(Basket basket, BasketItemRequest itemDto) {
        basket.getItems().removeIf(item -> item.getProduct()
                .getId()
                .equals(itemDto.getProductId()));
    }

    private Product findProductById(Long productId) {
        Product product = catalogService.getProduct(productId);
        return product;
    }

    private void saveBasket(Basket basket) {
        basket.setUpdatedAt(Instant.now());
        basketRepository.save(basket);
    }

    private void placeAnOrder(Basket basket) {
       
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setStatus(OrderStatus.CREATED);
        orderRequest.setUserId(basket.getUserId().toString());
        orderRequest.setOrderItems(basket.getItems()
                                    .stream()
                                    .map(this::getOrderItemDTO)
                                    .toList());
        Double totalAmount = basket.getItems()
                    .stream()
                    .mapToDouble(
                            item -> 
                            item.getProduct().getPrice() 
                                        * item.getQuantity())
                    .sum();
        orderRequest.setTotalAmount(totalAmount);
        orderService.createOrder(orderRequest);
    }

    private OrderItemRequest getOrderItemDTO(BasketItem basketItem) {
        return OrderItemRequest.builder()
                .productId(basketItem.getProduct().getId())
                .productName(basketItem.getProduct().getName())
                .price(basketItem.getProduct().getPrice())
                .quantity(basketItem.getQuantity())
                .build();
    }
}

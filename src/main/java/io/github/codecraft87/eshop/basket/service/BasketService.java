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
import io.github.codecraft87.eshop.catalog.service.ProductService;
import io.github.codecraft87.eshop.common.enums.BasketStatus;
import io.github.codecraft87.eshop.common.enums.OrderStatus;
import io.github.codecraft87.eshop.exceptions.BasketNotFoundException;
import io.github.codecraft87.eshop.notification.service.NotificationService;
import io.github.codecraft87.eshop.order.dto.OrderDTO;
import io.github.codecraft87.eshop.order.dto.OrderItemDTO;
import io.github.codecraft87.eshop.order.service.OrderService;
import jakarta.transaction.Transactional;

@Service
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductService productService;
    private final OrderService orderService;
    private final NotificationService notificationService;

    public BasketService(BasketRepository basketRepository,
            ProductService productService,
            OrderService orderService,
            NotificationService notificationService) {
        this.basketRepository = basketRepository;
        this.productService = productService;
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

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
        Product product = productService.getProductDetails(productId);
        return product;
    }

    private void saveBasket(Basket basket) {
        basket.setUpdatedAt(Instant.now());
        basketRepository.save(basket);
    }

    private void placeAnOrder(Basket basket) {
       
        OrderDTO orderRequest = new OrderDTO();
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

    private OrderItemDTO getOrderItemDTO(BasketItem basketItem) {
        return OrderItemDTO.builder()
                .productId(basketItem.getProduct().getId())
                .productName(basketItem.getProduct().getName())
                .price(basketItem.getProduct().getPrice())
                .quantity(basketItem.getQuantity())
                .build();
    }
}

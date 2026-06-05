package org.orderpaymentsystem.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.orderpaymentsystem.common.enums.BasketStatus;
import org.orderpaymentsystem.common.enums.OrderStatus;
import org.orderpaymentsystem.dto.BasketItemDTO;
import org.orderpaymentsystem.dto.BasketItemRequest;
import org.orderpaymentsystem.dto.BasketItemResponse;
import org.orderpaymentsystem.dto.OrderDTO;
import org.orderpaymentsystem.dto.OrderItemDTO;
import org.orderpaymentsystem.entity.Basket;
import org.orderpaymentsystem.entity.BasketItem;
import org.orderpaymentsystem.entity.Product;
import org.orderpaymentsystem.exceptions.BasketNotFoundException;
import org.orderpaymentsystem.repository.BasketRepository;
import org.springframework.stereotype.Service;

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

    public Long saveBasket(BasketItemRequest basketRequest) {
        Basket basket = getActiveBasketForUser(basketRequest);
        if (basket == null) {
            basket = addItemsToBasket(basket, basketRequest);
        } else {
            updateBasket(basket, basketRequest);
        }
        notificationService.basketUpdated(basket.getId());
        return basket.getId();
    }

     public List<BasketItemResponse> getBasketDetails(Long userId) {
        BasketItemRequest basketRequst = new BasketItemRequest();
        basketRequst.setUserId(userId);
        Basket basket = getActiveBasketForUser(basketRequst);
        
        List<BasketItemResponse> basketDetails = null;
        
        if(null!=basket){
        basketDetails = basket.getItems()
                .stream()
                .map(BasketItemResponse::getBasketItemResponse)
                .toList();
        }else{
            throw new BasketNotFoundException(userId);
        }
        return basketDetails;
    }

    @Transactional
    public void checkout(BasketItemRequest itemRequest) {
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

    private Basket getActiveBasketForUser(BasketItemRequest basketRequest) {
        Basket basket = basketRepository.findByUserIdAndStatus(
                basketRequest.getUserId(),
                BasketStatus.ACTIVE);
        return basket;
    }

    private Basket addItemsToBasket(
            Basket basket,
            BasketItemRequest basketRequest) {
        basket = new Basket();
        List<BasketItem> basketItems = new ArrayList<BasketItem>();
        for (BasketItemDTO item : basketRequest.getItems()) {
            Product product = findProductById(item.getProductId());
            BasketItem basketItem = new BasketItem();
            basketItem.setBasket(basket);
            basketItem.setProduct(product);
            basketItem.setQuantity(item.getQuantity());
            basketItems.add(basketItem);
        }
        basket.setItems(basketItems);
        basket.setUserId(basketRequest.getUserId());
        markBasketStatusActive(basket);
        saveBasket(basket);
        return basket;
    }

    private void markBasketStatusActive(Basket basket) {
        basket.setStatus(BasketStatus.ACTIVE);
        Instant now = Instant.now();
        basket.setCreatedAt(now);
    }

    private void updateBasket(
            Basket basket,
            BasketItemRequest basketRequest) {

        for (BasketItemDTO itemDto : basketRequest.getItems()) {
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

    private void syncBasket(Basket basket, BasketItemRequest basketRequest) {
        basket.getItems().removeIf(basketItem -> basketRequest.getItems().stream()
                .noneMatch(item -> item.getProductId().equals(basketItem.getProduct().getId())));
    }

    private void removeProductFromBasket(Basket basket, BasketItemDTO itemDto) {
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
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus(OrderStatus.CREATED);
        orderDTO.setUserId(basket.getUserId().toString());
        orderDTO.setOrderItems(basket.getItems()
                .stream()
                .map(this::getOrderItemDTO)
                .toList());
        double totalAmount = basket.getItems().stream()
        .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
        .sum();
        System.out.println("Total amount "+totalAmount);
        orderDTO.setTotalAmount(totalAmount);
        orderService.createOrder(orderDTO);
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

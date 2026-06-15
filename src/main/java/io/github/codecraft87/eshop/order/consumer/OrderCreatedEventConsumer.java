package io.github.codecraft87.eshop.order.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.catalog.entity.Product;
import io.github.codecraft87.eshop.catalog.service.CatalogModuleService;
import io.github.codecraft87.eshop.messaging.config.QueueConstants;
import io.github.codecraft87.eshop.messaging.event.BasketCheckedOutEvent;
import io.github.codecraft87.eshop.messaging.event.BasketItemEvent;
import io.github.codecraft87.eshop.messaging.event.OrderCreatedEvent;
import io.github.codecraft87.eshop.order.dto.OrderItemRequest;
import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.publisher.OrderCreatedEventPublisher;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreatedEventConsumer {

    private final OrderCreatedEventPublisher orderEventPublisher;
    private final CatalogModuleService productService;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    // where is exchange and routing id
    @RabbitListener(queues = QueueConstants.ORDER_BASKET_CHECKOUT_QUEUE)
    public void handleBasketCheckedOutEvent( String payload){
        
        BasketCheckedOutEvent checkedOutEvent =
                objectMapper.readValue(
                        payload,
                        BasketCheckedOutEvent.class);
        if(null!=checkedOutEvent) {
        log.info("Received BasketCheckedOutEvent for basket {}"+checkedOutEvent.basketId());
        
        OrderRequest request = new OrderRequest();
        request.setUserId(checkedOutEvent.userId().toString());
        request.setOrderItems(checkedOutEvent.items()
                            .stream()
                            .map(this::getOrderItemDTO)
                            .toList());

        Double totalAmount = request.getOrderItems()
                    .stream()
                    .mapToDouble(
                            item -> 
                            item.getPrice() 
                                        * item.getQuantity())
                    .sum();
        request.setTotalAmount(totalAmount);
        orderService.createOrder(request);
        log.info("Order created from publisher");
        orderEventPublisher.publishOrderCreatedEvent(
            new OrderCreatedEvent(checkedOutEvent.basketId(),
             checkedOutEvent.userId()));
        }else {
            log.info("Event is null");
        }
    }

    private OrderItemRequest getOrderItemDTO(BasketItemEvent basketItem) {
        OrderItemRequest orderItem = OrderItemRequest.builder()
                .productId(basketItem.productId())
                .quantity(basketItem.quantity())
                .build();

        Product product = productService.getProduct(orderItem.getProductId());
        orderItem.setProductName(product.getName());
        orderItem.setPrice(product.getPrice());
        return orderItem;
    }
}

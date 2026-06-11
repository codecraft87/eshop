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
import io.github.codecraft87.eshop.order.publisher.OrderEventPublisher;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderEventPublisher orderEventPublisher;
    private final CatalogModuleService productService;
    private final OrderService orderService;

    // where is exchange and routing id
    @RabbitListener(queues = QueueConstants.ORDER_BASKET_CHECKOUT_QUEUE)
    public void rabbitMQListener(BasketCheckedOutEvent checkedOutEvent){
        log.info("BasketCheckedOutEvent received for basket {}"+checkedOutEvent.basketId());
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
        orderEventPublisher.sendOrderCreatedEvent(
            new OrderCreatedEvent(checkedOutEvent.basketId(),
             checkedOutEvent.userId()));
        log.info("Order created ");
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

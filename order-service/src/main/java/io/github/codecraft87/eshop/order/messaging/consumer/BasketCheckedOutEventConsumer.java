package io.github.codecraft87.eshop.order.messaging.consumer;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messagingcommon.idempotency.EventIdempotency;
import io.github.codecraft87.eshop.order.dto.OrderItemRequest;
import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.messaging.config.OrderQueueConstants;
import io.github.codecraft87.eshop.order.messaging.event.BasketCheckedOutEvent;
import io.github.codecraft87.eshop.order.messaging.event.BasketItemEvent;
import io.github.codecraft87.eshop.order.messaging.event.OrderCreated;
import io.github.codecraft87.eshop.order.messaging.outbox.OrderOutboxService;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketCheckedOutEventConsumer {

  private final OrderService orderService;
  private final ObjectMapper objectMapper;
  private final EventIdempotency processedEventIdempotency;
  private final OrderOutboxService orderOutboxEventService;

  @RabbitListener(queues = OrderQueueConstants.ORDER_BASKET_CHECKOUT_QUEUE)
  public void handleBasketCheckedOutEvent(String payload) {

    BasketCheckedOutEvent checkedOutEvent = objectMapper.readValue(payload, BasketCheckedOutEvent.class);
    if (null != checkedOutEvent) {

      UUID eventId = UUID.fromString(checkedOutEvent.eventId());
      log.info("Received basket checkout event {} ", eventId);
      if (processedEventIdempotency.isEventProcessed(eventId)) {
        log.warn("Duplicate event {} ignored", eventId);
        return;
      }
      createOrder(checkedOutEvent);
      processedEventIdempotency.saveProcessedEvent(eventId);
      orderOutboxEventService.saveOrderCreatedEvent(
          new OrderCreated(checkedOutEvent.basketId(), checkedOutEvent.userId(), null));
    } else {
      log.warn("Event is null");
    }
  }

  private void createOrder(BasketCheckedOutEvent checkedOutEvent) {
    OrderRequest request = new OrderRequest();
    request.setUserId(checkedOutEvent.userId().toString());
    request.setOrderItems(checkedOutEvent.items().stream().map(this::getOrderItemDTO).toList());

    Double totalAmount = request.getOrderItems().stream()
        .mapToDouble(item -> item.getPrice() * item.getQuantity())
        .sum();
    request.setTotalAmount(totalAmount);
    orderService.createOrder(request);
  }

  private OrderItemRequest getOrderItemDTO(BasketItemEvent basketItem) {
    OrderItemRequest orderItem = OrderItemRequest.builder()
        .productId(basketItem.productId())
        .quantity(basketItem.quantity())
        .build();

    orderItem.setProductName(basketItem.productName());
    orderItem.setPrice(basketItem.price());
    return orderItem;
  }
}

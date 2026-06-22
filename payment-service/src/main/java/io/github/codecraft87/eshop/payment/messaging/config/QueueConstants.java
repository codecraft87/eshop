package io.github.codecraft87.eshop.payment.messaging.config;

public class QueueConstants {

  public static final String ORDER_BASKET_CHECKOUT_QUEUE = "order.basket.checkout.queue";

  public static final String BASKET_ORDER_CREATED_QUEUE = "basket.order.created.queue";

  public static final String PAYMENT_ORDER_PAYMENT_REQUESTED_QUEUE =
      "payment.order.payment.requested.queue";

  public static final String ORDER_PAYMENT_COMPLETED_QUEUE = "order.payment.completed.queue";

  public static final String ORDER_PAYMENT_FAILED_QUEUE = "order.payment.failed.queue";
}

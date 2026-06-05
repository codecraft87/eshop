package org.orderpaymentsystem.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    public void orderCreated(Long orderId){
        log.info("Notification: Order {} created "+orderId);
    }

    public void orderCancelled(Long orderId){
        log.info("Notification: Order {} cancelled", orderId);
    }

    public void orderUpdated(Long orderId){
        log.info("Notification: Order {} updated",orderId);
    }

    public void paymentSucceeded(Long paymentId){
        log.info("Notification: Payment {} succeeded", paymentId);
    }

    public void paymentFailed(Long paymentId){
        log.info("Notification: Payment {} failed ", paymentId);
    }

    public void paymentCancelled(Long orderId){
        log.info("Notification: Payment cancelled for order {} ", orderId);
    }

    public void basketUpdated(Long basketId){
        log.info("Notification: Basket {} updated ",basketId);
    }

    public void basketCheckout(long basketId){
        log.info("Notification: Basket {} checked out", basketId);
    }

    public void productCreated(Long productId) {
       log.info("Notification: product {} created", productId);
    }

    public void productUpdated(Long productId) {
        log.info("Notification: product {} updated", productId);
    }

    public void productDeleted(Long productId) {
        log.info("Notification: product {} deleted",productId);
    }
}

package io.github.codecraft87.eshop.notification.service;

public interface NotificationModuleService {

    void orderCreated(Long orderId);

    void orderCancelled(Long orderId);
    
    void orderUpdated(Long orderId);
    
    void paymentSucceeded(Long paymentId);
    
    void paymentFailed(Long paymentId);
    
    void paymentCancelled(Long orderId);
    
    void basketUpdated(Long basketId);
    
    void basketCheckout(long basketId);
    
    void productCreated(Long productId);
    
    void productUpdated(Long productId);
    
    void productDeleted(Long productId);
}

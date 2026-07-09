package io.github.codecraft87.eshop.basket.exceptions;

public class BasketNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  private Long basketId;
  
  public Long getBasketId() {
      return basketId;
  }
  
  public BasketNotFoundException(Long basketId) {
      this.basketId = basketId;
  }
}

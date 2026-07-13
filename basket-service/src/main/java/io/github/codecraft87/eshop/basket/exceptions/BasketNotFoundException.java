package io.github.codecraft87.eshop.basket.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.basket.enums.ErrorEnums;

public class BasketNotFoundException extends BasketException {

    private static final long serialVersionUID = 1L;

    private Long basketId;

    public Long getBasketId() {
        return basketId;
    }

    public BasketNotFoundException(Long basketId) {
        this.basketId = basketId;
    }

    @Override
    public String getErrorMessage() {
        return String.format(ErrorEnums.BASKET_NOT_FOUND.getErrorMessage(), this.id);
    }

    @Override
    public HttpStatus geHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorCode() {
        return ErrorEnums.BASKET_NOT_FOUND.getErrorCode();
    }
}

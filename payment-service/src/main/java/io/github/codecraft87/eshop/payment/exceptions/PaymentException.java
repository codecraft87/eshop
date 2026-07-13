package io.github.codecraft87.eshop.payment.exceptions;

import org.springframework.http.HttpStatus;

public abstract class PaymentException extends RuntimeException {

    protected String id;

    abstract protected String getErrorMessage();

    abstract public HttpStatus geHttpStatus();

    abstract public String getErrorCode();

}

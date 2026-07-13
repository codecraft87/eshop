package io.github.codecraft87.eshop.order.exceptions;

import org.springframework.http.HttpStatus;

public abstract class OrderException extends RuntimeException {

    protected String id;

    abstract protected String getErrorMessage();

    abstract public HttpStatus geHttpStatus();

    abstract public String getErrorCode();
}

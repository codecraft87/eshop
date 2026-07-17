package io.github.codecraft87.eshop.security.exception;

import org.springframework.http.HttpStatus;

public abstract class SecurityException extends RuntimeException {

    protected String id;

    abstract protected String getErrorMessage();

    abstract public HttpStatus geHttpStatus();

    abstract public String getErrorCode();

}

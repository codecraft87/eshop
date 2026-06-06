package io.github.codecraft87.eshop.exceptions;

public class DuplicatePaymentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicatePaymentException(Long id) {
        super("Duplicate payment detected for order [" + id + "]");
    }
}

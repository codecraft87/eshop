package io.github.codecraft87.eshop.securitycommon;

public class InvalidJwtTokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidJwtTokenException() {
        super("Invalid JWT token");
    }
}

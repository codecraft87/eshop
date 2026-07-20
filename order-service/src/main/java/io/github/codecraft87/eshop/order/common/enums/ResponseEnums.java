package io.github.codecraft87.eshop.order.common.enums;

public enum ResponseEnums {
    ORDER_CREATED("SUCCESS", "Order created"),
    ORDER_CANCELLED("SUCCESS", "Order cancelled"),
    ORDER_RETRIEVED("SUCCESS", "Order details"),
    ORDER_PROCSSED("SUCCESS", "Order procssed");

    private final String status;

    private final String message;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    private ResponseEnums(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

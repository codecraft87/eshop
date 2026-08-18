package io.github.codecraft87.eshop.order.common.enums;

public enum ErrorEnums {
        ORDER_NOT_FOUND(
                        "ORDER_NOT_FOUND",
                        "Order not found [%s]"),
        ORDER_ALREADY_CANCELLED(
                        "ORDER_ALREADY_CANCELLED",
                        "Order [%s] is already cancelled"),
        ORDER_CANNOT_BE_MODIFIED(
                        "ORDER_CANNOT_BE_MODIFIED",
                        "Once payment initiated, Order [%s] can not be modified"),
        CANCELLED_ORDER_CANNOT_BE_MODIFIED(
                        "CANCELLED_ORDER_CANNOT_BE_MODIFIED",
                        "Cancelled order [%s] can not be modified"),
        INVALID_ORDER_STATE(
                        "INVALID_ORDER_STATE",
                        "Payment is not allowed for this order [%s]"),
        DB_NOT_AVAILABLE(
                        "DB_NOT_AVAILABLE",
                        "Database is currently unavailable. Please try again later.");

        private final String errorCode;

        private final String errorMessage;

        public String getErrorMessage() {
                return errorMessage;
        }

        public String getErrorCode() {
                return errorCode;
        }

        private ErrorEnums(String errCode, String errorMessage) {
                this.errorCode = errCode;
                this.errorMessage = errorMessage;
        }
}

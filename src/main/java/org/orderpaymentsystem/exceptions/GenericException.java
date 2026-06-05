package org.orderpaymentsystem.exceptions;

public class GenericException extends RuntimeException {
  private static final long serialVersionUID = 1L;

    public GenericException(String errMsg){
         super(errMsg);
    }
}

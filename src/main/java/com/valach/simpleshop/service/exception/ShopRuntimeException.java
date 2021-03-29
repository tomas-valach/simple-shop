package com.valach.simpleshop.service.exception;

public class ShopRuntimeException extends RuntimeException {

  public ShopRuntimeException(String message, Object... messageParams) {
    super(formatMessage(message, messageParams));
  }

  public ShopRuntimeException(String message, Throwable cause, Object... messageParams) {
    super(formatMessage(message, messageParams), cause);
  }

  private static String formatMessage(String message, Object[] messageParams) {
    return String.format(message, messageParams);
  }
}

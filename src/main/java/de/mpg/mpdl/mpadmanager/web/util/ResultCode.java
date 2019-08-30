package de.mpg.mpdl.mpadmanager.web.util;

public enum ResultCode implements IErrorCode {
  SUCCESS(200, "success"),
  FAILED(500, "failed"),
  VALIDATE_FAILED(404, "wrong params"),
  UNAUTHORIZED(401, "token invalid"),
  FORBIDDEN(403, "permission denied");
  private long code;
  private String message;

  private ResultCode(long code, String message) {
      this.code = code;
      this.message = message;
  }

  public long getCode() {
      return code;
  }

  public String getMessage() {
      return message;
  }
}
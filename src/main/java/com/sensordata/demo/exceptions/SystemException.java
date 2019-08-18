package com.sensordata.demo.exceptions;

public class SystemException extends Exception {

    private ErrorCode errorCode;
    private String message;

    public SystemException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public SystemException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public SystemException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = code;
    }
}

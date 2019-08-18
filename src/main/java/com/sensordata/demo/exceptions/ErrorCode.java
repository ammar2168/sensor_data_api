package com.sensordata.demo.exceptions;

public enum ErrorCode {

    PARSE_ERROR(102, "Parse error"),
    INVALID_UUID(103, "Invalid UUID"),
    INVALID_PARAMETER(104, "Invalid parameter")
    ;


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public String getMessage() {
        return message;
    }
}

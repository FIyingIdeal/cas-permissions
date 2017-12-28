package com.hiynn.caspermissions.core.entity;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author yanchao
 * @date 2017/12/26 16:16
 */
public class HiynnResponseEntity implements Serializable {

    private int httpStatusCode;
    private String message;
    private Object result;

    public HiynnResponseEntity() {}

    private HiynnResponseEntity(Builder builder) {
        this.httpStatusCode = builder.httpStatus.value();
        this.message = builder.message == null ? builder.httpStatus.getReasonPhrase() : builder.message;
        this.result = builder.result;
    }

    public static class Builder {
        private HttpStatus httpStatus;
        private String message;
        private Object result;

        public Builder() {}

        public Builder(HttpStatus httpStatus, String message, Object result) {
            this.httpStatus = httpStatus;
            this.message = message;
            this.result = result;
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder result(Object result) {
            this.result = result;
            return this;
        }

        public HiynnResponseEntity build() {
            return new HiynnResponseEntity(this);
        }
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "HiynnResponseEntity{" +
                "httpStatusCode=" + httpStatusCode +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}

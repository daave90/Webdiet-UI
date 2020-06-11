package pl.daveproject.webdietui.exception;

import lombok.Getter;

@Getter
public class WebdietRestException extends RuntimeException {

    private String message;
    private ErrorCode errorCode;

    public WebdietRestException(String message) {
        this.message = message;
    }

    public WebdietRestException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }
}

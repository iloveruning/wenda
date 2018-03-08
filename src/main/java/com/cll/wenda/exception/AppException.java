package com.cll.wenda.exception;

public class AppException extends RuntimeException {

    private Object appendix;

    public AppException(String message, Object appendix) {
        super(message);
        this.appendix = appendix;
    }

    public AppException(String message) {
        super(message);
    }

    public Object getAppendix() {
        return appendix;
    }

    public void setAppendix(Object appendix) {
        this.appendix = appendix;
    }
}

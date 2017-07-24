package io.tchepannou.k.party.exception;

public class BusinessException extends RuntimeException {
    private Error errorCode;

    public BusinessException(final Error errorCode) {
        this.errorCode = errorCode;
    }
    public BusinessException(final Error errorCode, final Throwable cause) {
        super(cause);

        this.errorCode = errorCode;
    }

    public Error getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getCode() + " - " + errorCode.getDescription();
    }
}

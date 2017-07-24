package io.tchepannou.k.party.exception;

public class InvalidRequestException extends BusinessException{
    public InvalidRequestException(final Error errorCode) {
        super(errorCode);
    }
    public InvalidRequestException(final Error errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}

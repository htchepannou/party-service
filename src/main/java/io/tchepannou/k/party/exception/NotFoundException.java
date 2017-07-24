package io.tchepannou.k.party.exception;

public class NotFoundException extends BusinessException{
    public NotFoundException(final Error errorCode) {
        super(errorCode);
    }
}

package io.tchepannou.k.party.exception;

public enum Error {
    INVALID_ROLE_NAME("INVALID_ROLE", "Invalid role name"),
    INVALID_BIRTHDATE_FORMAT("INVALID_BIRTHDATE_FORMAT", "Birth date is invalid. Expecfing yyyy-MM-dd"),
    INVALID_GENDER("INVALID_GENDER", "Gender is invalid. Expecfing M or F"),
    PARTY_NOT_FOUND("PARTY_NOT_FOUND", "Party not found"),
    PARTY_ROLE_FOUND("PARTY_ROLE_FOUND", "PartyRole not found"),
    ;

    private String code;
    private String description;

    Error(String code, String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

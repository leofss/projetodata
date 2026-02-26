package com.leonardo.projetodata.enumerator;

public enum ErrorMessage {
    RAW_MATERIAL_NOT_FOUND("Raw Material not found"),

    RAW_MATERIAL_CODE_ALREADY_EXISTS("Raw Material with code %s already exists"),

    PRODUCT_NOT_FOUND("Product not found"),

    PRODUCT_CODE_ALREADY_EXISTS("Product with code %s already exists"),

    DUPLICATE_RAW_MATERIAL_IN_PRODUCT("Raw material is duplicated in the product composition"),

    INVALID_FIELD("Invalid fields");



    private final String messageTemplate;

    ErrorMessage(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessage(Object... params) {
        return String.format(this.messageTemplate, params);
    }
}

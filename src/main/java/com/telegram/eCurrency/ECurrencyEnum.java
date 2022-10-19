package com.telegram.eCurrency;

public enum ECurrencyEnum {
    btc("Биткоин"),
    xrp("Риппл");
    String value;

    ECurrencyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

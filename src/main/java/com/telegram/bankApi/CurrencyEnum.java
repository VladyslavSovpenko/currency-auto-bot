package com.telegram.bankApi;

public enum CurrencyEnum {
    USD("USD","USD", 840),
    UAH("UAH","UAH", 980),
    EUR("EUR","EUR", 978),
    RUB("RUB","RUB", 643),
    EURUSD("EURUSD");

    String value;
    int isoCode;
    String name;

    CurrencyEnum(String value, String name, int isoCode) {
        this.value = value;
        this.isoCode = isoCode;
        this.name=name;
    }
    CurrencyEnum(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public int getIsoCode() {
        return isoCode;
    }
    public String getName(){
        return name;
    }
}

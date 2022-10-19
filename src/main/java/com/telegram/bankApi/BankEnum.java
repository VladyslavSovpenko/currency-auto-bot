package com.telegram.bankApi;

import lombok.SneakyThrows;

import java.lang.reflect.Method;

public enum BankEnum {
    PRIVATBANK("Приватбанк", "PRIVATBANK",  PrivatBankApi.class),
    MONOBANK("Монобанк", "MONOBANK", MonoBankApi.class),
    NBU("НБУ", "NBU", NbuApi.class);

    private final String value;
    private final String name;
    private final Method method; //свойство для хранения метода, вызывающего по АПИ соответствующий банк

    @SneakyThrows
    BankEnum(String value, String name, Class<?> cl) {
        this.value = value;
        this.method = cl.getMethod("getListOfCurrenciesRate");
        this.name=name;
    }

    public String getValue() {
        return value;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }
}

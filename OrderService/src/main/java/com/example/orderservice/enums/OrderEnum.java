package com.example.orderservice.enums;

public enum OrderEnum {
    PAID(1),UNPAID(2);

    private int value;

    OrderEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

package com.example.userservice.enums;

public enum RoleEnums {
    ADMIN(1),
    USER(2);

    private int value;

    RoleEnums(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

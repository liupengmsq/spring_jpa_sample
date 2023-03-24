package com.example.example1;

public enum Gender {
    MAIL("男性"), FMAIL("女性");

    private String value;

    private Gender(String value) {
        this.value = value;
    }
}

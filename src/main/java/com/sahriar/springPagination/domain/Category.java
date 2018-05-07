package com.sahriar.springPagination.domain;

public enum  Category {

    SPORTS("Sports"),
    TECH("Technology");

    private final String name;

    private Category(String name){
        this.name = name;
    }

}

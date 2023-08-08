package com.fx.domain;

public class GreetingDto {
    public String name;

    public GreetingDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

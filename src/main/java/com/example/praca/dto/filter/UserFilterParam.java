package com.example.praca.dto.filter;

/**
 * @author Daniel Lezniak
 */
public enum UserFilterParam {

    AH("All with hobby"),
    AHN("All with hobby and newsletter");

    public final String name;
    private UserFilterParam(String name) {
        this.name = name;
    }
}

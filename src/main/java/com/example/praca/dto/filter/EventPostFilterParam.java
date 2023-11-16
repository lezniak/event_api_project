package com.example.praca.dto.filter;

/**
 * @author Daniel Lezniak
 */
public enum EventPostFilterParam {
    AP("all post in event"),
    EO("only event owner"),
    AU("user post");

    public final String name;
    private EventPostFilterParam(String name) {
        this.name = name;
    }
}

package com.example.praca.dto;

/**
 * @author Daniel Lezniak
 */
public enum PlaceFilterParam {
    C("capacity");

    public final String name;
    private PlaceFilterParam(String name) {
        this.name = name;
    }
}

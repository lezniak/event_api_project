package com.example.praca.dto.filter;

/**
 * @author Daniel Lezniak
 */
public enum EventOrganizationFilter {
    A("All for user"),
    E("All for event");

    public final String name;
    private EventOrganizationFilter(String name) {
        this.name = name;
    }
}

package com.example.praca.dto.task;

/**
 * @author Daniel Lezniak
 */
public enum TaskFilterParam {
    UO("user organization"),
    O("organization"),
    U("only one user task"),
    UE("user in event");

    public final String name;
    private TaskFilterParam(String name) {
        this.name = name;
    }
}

package com.example.praca.dto.task;

/**
 * @author Daniel Lezniak
 */
public enum TaskStatus {
    W("WAITING"),
    P("IN PROGRESS"),
    D("DONE");

    public final String name;
    private TaskStatus(String name) {
        this.name = name;
    }
}

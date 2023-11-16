package com.example.praca.dto.filter;

/**
 * @author Daniel Lezniak
 */
public enum EventFilterParam {
    C("city"),
    D("date"),
    DD("dateRange"),
    O("owner"),
    A("all"),
    T("types"),
    P("participant"),

    EM("eventMembers"),
    DDT("eventTypeAndDateRange"),
    TA("To accepted"),
    MH("member history event"),
    ACCEPTED("accepted"),
    SCH("scheduler");

    public final String name;
    private EventFilterParam(String name) {
        this.name = name;
    }
}

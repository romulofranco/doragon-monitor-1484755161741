package com.ibm.doragon.monitor.backend.data;
public enum Availability {
    FAILURE("Failure"), OK("OK");

    private final String name;

    private Availability(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

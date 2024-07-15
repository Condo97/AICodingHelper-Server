package com.aicodinghelper.database.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Subscriptions {

    FREE("FREE"),
    LOW_WEEKLY("LOW_WEEKLY"),
    LOW_MONTHLY("LOW_MONTHLY"),
    MEDIUM_WEEKLY("MEDIUM_WEEKLY"),
    MEDIUM_MONTHLY("MEDIUM_MONTHLY"),
    HIGH_WEEKLY("HIGH_WEEKLY"),
    HIGH_MONTHLY("HIGH_MONTHLY");

    private String string;

    @JsonCreator
    Subscriptions(String string) {
        this.string = string;
    }

    @JsonValue
    public String getString() {
        return string;
    }

}

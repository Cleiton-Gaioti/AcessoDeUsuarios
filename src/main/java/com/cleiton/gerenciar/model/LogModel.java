package com.cleiton.gerenciar.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("log")
public final class LogModel {
    // ATTRIBUTES
    private final String operation;
    private final String name;
    private final LocalDate date;
    private final LocalTime hour;
    private final String username;
    private final String exception;

    // CONSTRUCTOR

    public LogModel(String operation, String name, LocalDate date, LocalTime hour, String username, String exception) {
        this.operation = operation;
        this.name = name;
        this.date = date;
        this.hour = hour;
        this.username = username;
        this.exception = exception;
    }

    // GETTERS AND SETTERS

    public String getOperation() {
        return this.operation;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getHour() {
        return this.hour;
    }

    public String getUsername() {
        return this.username;
    }

    public String getException() {
        return this.exception;
    }

}

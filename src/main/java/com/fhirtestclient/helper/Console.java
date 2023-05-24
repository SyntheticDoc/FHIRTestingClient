package com.fhirtestclient.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Console {
    private static Console instance;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss:SSS");

    private Console() {}

    public static Console getInstance() {
        if(instance == null) {
            instance = new Console();
        }

        return instance;
    }

    public void out(Object source, String message) {
        printSystem(source.getClass().getSimpleName(), message);
    }

    public void debug(Object source, String message) {
        printDebug(source.getClass().getSimpleName(), message);
    }

    public void err(Object source, String message) {
        printError(source.getClass().getSimpleName(), message);
    }

    private void printSystem(String source, String message) {
        printSimple("System", source, message);
    }

    private void printDebug(String source, String message) {
        printSimple("Debug", source, message);
    }

    private void printError(String source, String message) {
        printSimple("Error", source, message);
    }

    private void printSimple(String type, String source, String message) {
        if(type.equalsIgnoreCase("ERROR")) {
            System.err.printf("%s %6s: %s: %s%n", getTimestamp(), type.toUpperCase(), source, message);
        } else {
            System.out.printf("%s %6s: %s: %s%n", getTimestamp(), type.toUpperCase(), source, message);
        }
    }

    private String getTimestamp() {
        return dtf.format(LocalDateTime.now());
    }
}

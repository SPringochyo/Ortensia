package dev.springochyo.ortensia.utils;

public class CycleException extends RuntimeException {
    public CycleException(String message) {
        super(message);
    }
}

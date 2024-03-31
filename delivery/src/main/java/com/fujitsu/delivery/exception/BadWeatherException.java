package com.fujitsu.delivery.exception;

// This class is a custom exception class for a bad weather.
public class BadWeatherException extends RuntimeException {
    public BadWeatherException() {
        super();
    }

    public BadWeatherException(String message) {
        super(message);
    }

    public BadWeatherException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadWeatherException(Throwable cause) {
        super(cause);
    }
}

package com.amcglynn.myenergi.exception;

public class ClientException extends RuntimeException {
    public ClientException(int statusCode) {
        super("Failed with status code " + statusCode);
    }
}

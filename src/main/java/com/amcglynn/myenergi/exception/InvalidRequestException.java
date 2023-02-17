package com.amcglynn.myenergi.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(int status, String statusText) {
        super("Failed with status " + status + " and status text \"" + statusText + "\"");
    }
}

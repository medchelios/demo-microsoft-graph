package com.tmoh.demomicrosoftgraph.exception;

public class GraphClientException extends RuntimeException{
    public GraphClientException(String message) {
        super(message);
    }

    public GraphClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

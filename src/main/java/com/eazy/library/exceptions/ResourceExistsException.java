package com.eazy.library.exceptions;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String message){
        super(message);
    }
}

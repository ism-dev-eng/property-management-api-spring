package com.propertymanagement.domain.exception;

import java.util.UUID;

public class PropertyNotFoundException extends RuntimeException {
    
    public PropertyNotFoundException(UUID id) {
        super("Property with id " + id + " not found");
    }
    
    public PropertyNotFoundException(String message) {
        super(message);
    }
}
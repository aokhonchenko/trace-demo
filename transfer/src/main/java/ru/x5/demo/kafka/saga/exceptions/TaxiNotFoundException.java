package ru.x5.demo.kafka.saga.exceptions;

import org.springframework.core.NestedRuntimeException;

public class TaxiNotFoundException extends NestedRuntimeException {
    public TaxiNotFoundException(String msg) {
        super(msg);
    }
}

package ru.x5.demo.kafka.saga.exceptions;

import org.springframework.core.NestedRuntimeException;

public class TicketNotFoundException extends NestedRuntimeException {
    public TicketNotFoundException(String msg) {
        super(msg);
    }
}

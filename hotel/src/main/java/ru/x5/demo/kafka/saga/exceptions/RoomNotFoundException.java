package ru.x5.demo.kafka.saga.exceptions;

import org.springframework.core.NestedRuntimeException;

public class RoomNotFoundException extends NestedRuntimeException {
    public RoomNotFoundException(String msg) {
        super(msg);
    }
}

package ru.yandex.practicum.taskmanager.exceptions;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, IOException exception) {
        super(message);
    }
}

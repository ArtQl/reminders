package ru.artq.reminders.api.exception;

public class ListRemindersNotFoundException extends RuntimeException {
    public ListRemindersNotFoundException(String message) {
        super(message);
    }

    public ListRemindersNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

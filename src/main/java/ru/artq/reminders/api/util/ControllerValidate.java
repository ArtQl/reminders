package ru.artq.reminders.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.artq.reminders.api.exception.BadRequestException;

@Component
@Slf4j
public class ControllerValidate {
    public static void checkListId(Long id) {
        if (id == null || id <= 0) {
            log.info("List id: {} is not correct.", id);
            throw new BadRequestException("List id is not correct.");
        }
    }

    public static void checkReminderId(Long id) {
        if (id == null || id <= 0) {
            log.info("Reminder id: {} is not correct.", id);
            throw new BadRequestException("Reminder id is not correct.");
        }
    }

    public static void checkListName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.info("Name list: {} is not correct.", name);
            throw new BadRequestException("Name list is not correct.");
        }
    }
    public static void checkReminderName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.info("Name reminder: {} is not correct.", name);
            throw new BadRequestException("Name reminder is not correct.");
        }
    }
}

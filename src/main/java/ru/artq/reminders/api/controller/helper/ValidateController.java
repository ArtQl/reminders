package ru.artq.reminders.api.controller.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.artq.reminders.api.exception.BadRequestException;
import ru.artq.reminders.store.entity.ReminderPriority;

@Component
@Slf4j
public class ValidateController {
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

    public static void checkPriority(String priority) {
        if (priority != null && priority.trim().isEmpty()) {
            try {
                ReminderPriority.valueOf(priority);
            } catch (IllegalArgumentException e) {
                log.info("Priority name: {} is not correct.", priority);
                throw new IllegalArgumentException("Priority name: '%s' is not correct.".formatted(priority));
            }
        }
    }
}

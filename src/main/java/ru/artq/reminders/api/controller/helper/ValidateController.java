package ru.artq.reminders.api.controller.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.artq.reminders.api.exception.BadRequestException;

@Component
@Slf4j
public class ValidateController {
    public static void checkId(Long id) {
        if (id == null || id <= 0) {
            log.info("Id: {} is not correct.", id);
            throw new BadRequestException("Id: '%s' is not correct.".formatted(id));
        }
    }

    public static void checkTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            log.info("Title: {} is not correct.", title);
            throw new BadRequestException("Title: '%s' is not correct.".formatted(title));
        }
    }

    public static void checkUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.info("Username: {} is not correct.", username);
            throw new BadRequestException("Username: '%s' is not correct.".formatted(username));
        }
    }
}

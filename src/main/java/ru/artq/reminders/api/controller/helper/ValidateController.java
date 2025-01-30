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

    public static void checkUserParams(String username, String email, String password) {
        checkParams("Username", username);
        checkParams("Email", email);
        checkParams("Password", password);
    }

    private static void checkParams(String title, String param) {
        if (param == null || param.trim().isEmpty()) {
            log.info("{}: {} is not correct.", title, param);
            throw new BadRequestException("%s: '%s' is not correct.".formatted(title, param));
        }
    }
}

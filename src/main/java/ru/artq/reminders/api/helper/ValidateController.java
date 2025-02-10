package ru.artq.reminders.api.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.artq.reminders.core.exception.BadRequestException;

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

    public static void checkPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            log.info("Password: {} is not correct.", password);
            throw new BadRequestException("Password: '%s' is not correct.".formatted(password));
        }
    }

    public static void checkEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            log.info("Email: {} is not correct.", email);
            throw new BadRequestException("Email: '%s' is not correct.".formatted(email));
        }
    }
}

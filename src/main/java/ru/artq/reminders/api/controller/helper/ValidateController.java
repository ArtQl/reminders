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

    public static void checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.info("Name: {} is not correct.", name);
            throw new BadRequestException("Name: '%s' is not correct.".formatted(name));
        }
    }
}

package ru.artq.reminders.api.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@RequiredArgsConstructor
public class CommandConfiguration {
    private final CommandFactory commandFactory;
    private final RegistrationCommand registrationCommand;
    private final LoginCommand loginCommand;
    private final CreateReminderCommand createReminderCommand;
    private final FindReminderCommand findReminderCommand;

    @PostMapping
    public void registerCommands() {
        commandFactory.register("/registration", registrationCommand);
        commandFactory.register("/login", loginCommand);
        commandFactory.register("/new", createReminderCommand);
        commandFactory.register("/find", findReminderCommand);
    }
}

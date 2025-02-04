package ru.artq.reminders.api.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.artq.reminders.api.telegram.command.CreateReminderCommand;
import ru.artq.reminders.api.telegram.command.FindReminderCommand;
import ru.artq.reminders.api.telegram.command.LoginCommand;
import ru.artq.reminders.api.telegram.command.RegistrationCommand;

@Component
@RequiredArgsConstructor
public class CommandConfig {
    private final CommandFactory commandFactory;
    private final RegistrationCommand registrationCommand;
    private final LoginCommand loginCommand;
    private final CreateReminderCommand createReminderCommand;
    private final FindReminderCommand findReminderCommand;

    @PostConstruct
    public void registerCommands() {
        commandFactory.register("/registration", registrationCommand);
        commandFactory.register("/login", loginCommand);
        commandFactory.register("/new", createReminderCommand);
        commandFactory.register("/find", findReminderCommand);
    }
}

package ru.artq.reminders.api.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.artq.reminders.api.telegram.command.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

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

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public Optional<Command> getCommand(String commandName) {
        return Optional.ofNullable(commands.get(commandName));
    }
}

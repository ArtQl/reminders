package ru.artq.reminders.api.telegram.command;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandConfig {
    private final CommandFactory commandFactory;
    private final RegistrationCommand registrationCommand;
    private final LoginCommand loginCommand;
    private final CreateCommand createCommand;
    private final FindCommand findCommand;
    private final UpdateCommand updateCommand;
    private final DeleteCommand deleteCommand;

    @PostConstruct
    public void registerCommands() {
        commandFactory.register("/registration", registrationCommand);
        commandFactory.register("/login", loginCommand);
        commandFactory.register("/new", createCommand);
        commandFactory.register("/find", findCommand);
        commandFactory.register("/update", updateCommand);
        commandFactory.register("/delete", deleteCommand);
    }
}

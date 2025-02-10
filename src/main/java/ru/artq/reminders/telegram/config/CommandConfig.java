package ru.artq.reminders.telegram.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.artq.reminders.telegram.command.*;

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
    private final Environment env;

    @PostConstruct
    public void registerCommands() {
        commandFactory.register(env.getProperty("telegram.command.registration"), registrationCommand);
        commandFactory.register(env.getProperty("telegram.command.login"), loginCommand);
        commandFactory.register(env.getProperty("telegram.command.create"), createCommand);
        commandFactory.register(env.getProperty("telegram.command.find"), findCommand);
        commandFactory.register(env.getProperty("telegram.command.update"), updateCommand);
        commandFactory.register(env.getProperty("telegram.command.delete"), deleteCommand);
    }
}

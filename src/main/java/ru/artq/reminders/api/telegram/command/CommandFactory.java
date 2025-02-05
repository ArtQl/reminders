package ru.artq.reminders.api.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public Optional<Command> getCommand(String commandName) {
        return Optional.ofNullable(commands.get(commandName));
    }
}

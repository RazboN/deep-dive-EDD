package com.techbank.account.cmd.infrastructure;

import com.techbank.cqrs.core.commands.BaseCommand;
import com.techbank.cqrs.core.commands.CommandHandlerMethod;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AccountCommandDispatcher implements CommandDispatcher {
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes =
            new HashMap<>();
    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, e -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        var handlers = routes.get(command.getClass());

        if (handlers == null || handlers.size() == 0){
            log.error("{} - No command handler was registered!", this.getClass());
            throw new RuntimeException("No command handler was registered!");
        }

        if (handlers.size() > 1) {
            log.error("{} - Can not command to more than one handler!", this.getClass());
            throw new RuntimeException("Can not command to more than one handler!");
        }

        handlers.get(0).handle(command);
    }
}

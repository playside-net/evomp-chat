package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.game.entity.ped.Ped;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CommandManager {
    private final Chat script;
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(Chat script) {
        this.script = script;
    }

    public void execute(Ped player, String input) throws CommandException {
        String[] args = input.split(" ");
        String name = args[0];
        Command command = commands.get(name);
        if (command != null) {
            args = Arrays.copyOfRange(args, 1, args.length);
            command.execute(player, args);
        } else {
            throw new CommandException("Unknown command: " + name);
        }
    }

    public void register(Command command) {
        commands.put(command.name, command);
    }
}

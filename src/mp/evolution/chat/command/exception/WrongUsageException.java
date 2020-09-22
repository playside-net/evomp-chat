package mp.evolution.chat.command.exception;

import mp.evolution.chat.command.Command;

public class WrongUsageException extends CommandException {
    public WrongUsageException(Command command, String syntax) {
        super("Usage: /" + command.name + " " + syntax);
    }
}

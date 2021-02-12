package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.streaming.Model;
import mp.evolution.math.Hash;
import mp.evolution.script.Script;

public abstract class Command {
    public final Script script;
    public final String name;

    protected Command(Script script, String name) {
        this.script = script;
        this.name = name;
    }

    protected static int parseInt(String arg) throws CommandException {
        return parseInt(arg, 10);
    }

    protected static int parseInt(String arg, int radix) throws CommandException {
        try {
            return Integer.parseInt(arg, radix);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid integer value: " + arg);
        }
    }

    protected static float parseFloat(String arg) throws CommandException {
        try {
            return Float.parseFloat(arg);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid float value: " + arg);
        }
    }

    protected static float parseCoord(String arg, float origin) throws CommandException {
        if (arg.startsWith("~")) {
            return origin + arg.length() > 0 ? parseFloat(arg.substring(1)) : 0;
        } else {
            return parseFloat(arg);
        }
    }

    protected int parseHash(String arg) throws CommandException {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return Hash.joaat(arg);
        }
    }

    protected Model parseModel(String arg) throws CommandException {
        int hash = parseHash(arg);
        Model model = new Model(hash);
        if (!model.isValid()) {
            throw new CommandException("Invalid model: " + arg);
        }
        if (!model.isInCDImage()) {
            throw new CommandException("Model is not in CD image: " + arg);
        }
        return model;
    }

    public abstract void execute(Ped player, String[] args) throws CommandException;
}

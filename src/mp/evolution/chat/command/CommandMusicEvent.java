package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.script.Script;

public class CommandMusicEvent extends Command {
    public CommandMusicEvent(Script script) {
        super(script, "mev");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            String action = args[0];
            String name = args[1];
            switch (action) {
                case "load": {
                    boolean result = script.invokeBoolean(0x1E5185B72EF5158AL, name);
                    ((Chat) script).push("~w~Preparing sound event ~y~" + name + " ~w~" + result);
                    break;
                }
                case "play": {
                    boolean result = script.invokeBoolean(0x706D57B0F50DA710L, name);
                    ((Chat) script).push("~w~Triggering sound event ~y~" + name + " ~w~" + result);
                    break;
                }
                case "stop": {
                    boolean result = script.invokeBoolean(0x5B17A90291133DA5L, name);
                    ((Chat) script).push("~w~Cancelling sound event ~y~" + name + " ~w~" + result);
                    break;
                }
                default: {
                    throw new WrongUsageException(this, "play|stop <name>");
                }
            }
        } else {
            throw new WrongUsageException(this, "load|play|stop <name>");
        }
    }
}

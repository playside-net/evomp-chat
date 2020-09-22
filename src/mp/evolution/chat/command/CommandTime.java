package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.time.Clock;
import mp.evolution.script.Script;

public class CommandTime extends Command {
    private final Clock clock;

    public CommandTime(Script script) {
        super(script, "time");
        clock = new Clock(script);
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            int hour = parseInt(args[0]);
            int minute = parseInt(args[1]);
            clock.setTime(hour, minute, 0);
            ((Chat) script).push("~y~Set time to ~w~" + hour + ":" + minute);
        } else {
            throw new WrongUsageException(this, "");
        }
    }
}

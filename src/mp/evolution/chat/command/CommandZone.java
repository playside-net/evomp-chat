package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.gps.GPS;
import mp.evolution.math.Vector3;
import mp.evolution.script.Script;

public class CommandZone extends Command {
    private final GPS gps;

    public CommandZone(Script script) {
        super(script, "zone");
        gps = new GPS(script);
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 0) {
            Vector3 pos = player.getPosition();
            String zone = gps.getZoneName(pos);
            ((Chat) script).push("~y~Your zone is ~w~" + zone);
        } else {
            throw new WrongUsageException(this, "");
        }
    }
}

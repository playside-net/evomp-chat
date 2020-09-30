package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.math.Vector3;
import mp.evolution.script.Script;

public class CommandTeleport extends Command {
    public CommandTeleport(Script script) {
        super(script, "tp");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length >= 3) {
            Vector3 origin = player.getPosition();
            player.setPositionKeepVehicle(
                parseCoord(args[0], origin.x),
                parseCoord(args[1], origin.y),
                parseCoord(args[2], origin.z)
            );
        } else {
            throw new WrongUsageException(this, "<x> <y> <z>");
        }
    }
}

package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandInteriorColor extends Command {
    public CommandInteriorColor(Script script) {
        super(script, "uc");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                veh.setInteriorColor(parseInt(args[0], 16));
            }
        } else {
            throw new WrongUsageException(this, "<color>");
        }
    }
}
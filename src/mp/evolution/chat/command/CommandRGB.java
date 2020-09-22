package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandRGB extends Command {
    public CommandRGB(Script script) {
        super(script, "rgb");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                int primary = parseInt(args[0], 16);
                int secondary = parseInt(args[1], 16);
                veh.setPrimaryColorRGB(primary);
                veh.setSecondaryColorRGB(secondary);
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else {
            throw new WrongUsageException(this, "<primary> <secondary>");
        }
    }
}

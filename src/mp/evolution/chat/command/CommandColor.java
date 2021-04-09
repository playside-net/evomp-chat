package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandColor extends Command {
    public CommandColor(Script script) {
        super(script, "col");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                int variant = parseInt(args[0]);
                veh.setColorCombination(variant);
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else {
            throw new WrongUsageException(this, "[variant]");
        }
    }
}

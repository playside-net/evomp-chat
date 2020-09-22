package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandFuel extends Command {
    public CommandFuel(Script script) {
        super(script, "fuel");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                float fuel = parseFloat(args[0]);
                veh.setFuel(fuel);
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else if (args.length == 0) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                ((Chat) script).push("~y~Fuel level is ~w~" + veh.getFuel() + " / " + 65.0);
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else {
            throw new WrongUsageException(this, "[level]");
        }
    }
}

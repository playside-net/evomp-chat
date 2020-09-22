package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandOil extends Command {
    public CommandOil(Script script) {
        super(script, "oil");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                float fuel = parseFloat(args[0]);
                veh.setOil(fuel);
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else if (args.length == 0) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                ((Chat) script).push("~y~Oil level is ~w~" + veh.getOil());
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else {
            throw new WrongUsageException(this, "[level]");
        }
    }
}

package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandMod extends Command {
    public CommandMod(Script script) {
        super(script, "mod");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                int id = parseInt(args[0]);
                int value = parseInt(args[1]);
                veh.setModKit(0);
                veh.setMod(id, value, true);
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else {
            throw new WrongUsageException(this, "<model>");
        }
    }
}

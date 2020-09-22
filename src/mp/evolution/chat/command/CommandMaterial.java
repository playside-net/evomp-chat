package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandMaterial extends Command {
    public CommandMaterial(Script script) {
        super(script, "material");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                int primary = parseInt(args[0]);
                int secondary = parseInt(args[1]);
                veh.setColor(primary, secondary);
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else {
            throw new WrongUsageException(this, "<primary> <secondary>");
        }
    }
}

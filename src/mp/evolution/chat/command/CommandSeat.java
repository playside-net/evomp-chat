package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandSeat extends Command {
    public CommandSeat(Script script) {
        super(script, "seat");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            int seat = parseInt(args[0]);
            Vehicle veh = player.getVehicleIn(false);
            if (veh == null) {
                veh = player.getClosestVehicle(10);
            }
            if (veh != null) {
                if (veh.isSeatFree(seat)) {
                    player.taskEnterVehicle(veh, 5000, seat, 2, 1);
                } else {
                    throw new CommandException("Seat " + seat + " is already occupied");
                }
            } else {
                throw new CommandException("No vehicle found");
            }
        } else {
            throw new WrongUsageException(this, "<seat>");
        }
    }
}

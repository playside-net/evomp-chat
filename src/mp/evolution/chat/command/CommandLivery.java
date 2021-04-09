package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandLivery extends Command {
    public CommandLivery(Script script) {
        super(script, "livery");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                int livery = parseInt(args[0]);
                veh.setLivery(livery);
                veh.setModKit(0);
                message("~y~Texture variant set to ~w~" + livery + " (" + veh.getLiveryName(livery) + ") / " + veh.getLiveryCount());
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else if (args.length == 0) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                int livery = veh.getLivery();
                veh.setModKit(0);
                message("~y~Texture variant is ~w~" + livery + " (" + veh.getLiveryName(livery) + ") / " + veh.getLiveryCount());
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else {
            throw new WrongUsageException(this, "[variant]");
        }
    }
}

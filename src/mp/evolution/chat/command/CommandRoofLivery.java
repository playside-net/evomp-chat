package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.script.Script;

public class CommandRoofLivery extends Command {
    public CommandRoofLivery(Script script) {
        super(script, "rl");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                int livery = parseInt(args[0]);
                veh.setRoofLivery(livery);
                message("~y~Texture variant set to ~w~" + livery + " (" + veh.getLiveryName(livery) + ")");
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else if (args.length == 0) {
            Vehicle veh = player.getVehicleIn(false);
            if (veh != null) {
                int livery = veh.getRoofLivery();
                message("~y~Texture variant is ~w~" + livery + " (" + veh.getLiveryName(livery) + ") / " + veh.getRoofLiveryCount());
            } else {
                throw new CommandException("You're not in a vehicle");
            }
        } else {
            throw new WrongUsageException(this, "[variant]");
        }
    }
}

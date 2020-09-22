package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.game.streaming.Model;
import mp.evolution.script.Script;

public class CommandVehicle extends Command {
    public CommandVehicle(Script script) {
        super(script, "veh");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Model model = parseModel(args[0]);
            if (!model.isVehicle()) {
                throw new CommandException("Provided model is not a vehicle: " + model.name);
            }
            model.load(script, r -> {
                if (player.exists()) {
                    Vehicle veh = new Vehicle(script, model, player.getPosition(), player.getHeading());
                    player.putIntoVehicle(veh, -1);
                }
            });
        } else {
            throw new WrongUsageException(this, "<model>");
        }
    }
}

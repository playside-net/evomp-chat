package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.vehicle.Vehicle;
import mp.evolution.game.streaming.Model;
import mp.evolution.script.Script;

import java.util.ArrayList;

public class CommandTrain extends Command {
    private static final ArrayList<Model> TRAIN_MODELS = new ArrayList<>();

    static {
        TRAIN_MODELS.add(new Model("freight"));
        TRAIN_MODELS.add(new Model("freightcar"));
        TRAIN_MODELS.add(new Model("freightgrain"));
        TRAIN_MODELS.add(new Model("freightcont1"));
        TRAIN_MODELS.add(new Model("freightcont2"));
        TRAIN_MODELS.add(new Model("freighttrailer"));
        TRAIN_MODELS.add(new Model("tankercar"));
        TRAIN_MODELS.add(new Model("metrotrain"));
    }

    public CommandTrain(Script script) {
        super(script, "train");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            Model model = parseModel(args[0]);
            boolean direction = parseInt(args[1]) != 0;
            if (!model.isVehicle() || !TRAIN_MODELS.contains(model)) {
                throw new CommandException("Provided model is not a train: " + model.name);
            }
            model.load(script, r -> {
                if (player.exists()) {
                    Vehicle veh = script.invokeGeneric(Vehicle::new, 0x63C6CCA8E68AE8C8L, model.hash, player.getPosition(), direction);
                    player.putIntoVehicle(veh, -1);
                }
            });
        } else {
            throw new WrongUsageException(this, "<model>");
        }
    }
}

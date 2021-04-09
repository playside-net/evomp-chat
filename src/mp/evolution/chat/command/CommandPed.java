package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.streaming.Model;
import mp.evolution.script.Script;

public class CommandPed extends Command {
    public CommandPed(Script script) {
        super(script, "ped");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            Model model = new Model(args[0]);
            int type = parseInt(args[1]);
            model.load(script, m -> {
                Ped p = new Ped(script, type, model, player.getPosition(), player.getHeading());
                message("~y~New ped: ~w~" + p.handle());
            });
        } else {
            throw new WrongUsageException(this, "<model> <type>");
        }
    }
}

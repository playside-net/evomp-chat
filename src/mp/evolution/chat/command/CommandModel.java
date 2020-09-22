package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.player.Player;
import mp.evolution.game.streaming.Model;
import mp.evolution.script.Script;

public class CommandModel extends Command {
    public CommandModel(Script script) {
        super(script, "model");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Model model = new Model(args[0]);
            model.load(script, m -> {
                ((Chat) script).push("Old ped: " + Ped.local(script).handle());
                Player pl = Player.local(script);
                pl.setModel(m);
                Ped p = Ped.local(script);
                ((Chat) script).push("New ped: " + p.handle());
                p.setDefaultComponentVariation();
            });
        } else {
            throw new WrongUsageException(this, "<model>");
        }
    }
}

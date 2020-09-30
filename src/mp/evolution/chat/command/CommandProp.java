package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.audio.Sound;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.entity.prop.Prop;
import mp.evolution.game.streaming.Model;
import mp.evolution.script.Script;

public class CommandProp extends Command {
    public CommandProp(Script script) {
        super(script, "prop");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 1) {
            Model model = new Model(parseHash(args[0]));
            model.load(script, m -> {
                Sound sound = new Sound("PICK_UP", "HUD_FRONTEND_DEFAULT_SOUNDSET");
                Prop prop = new Prop(script, m, player.getPosition(), true);
                prop.placeOnGround();
                sound.playFrontend();
            });
        } else {
            throw new WrongUsageException(this, "<hash>");
        }
    }
}

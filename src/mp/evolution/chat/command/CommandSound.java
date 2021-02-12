package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.audio.Sound;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.script.Script;

public class CommandSound extends Command {
    public CommandSound(Script script) {
        super(script, "sound");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length > 0) {
            String name = args[0];
            String set = args.length > 1 ? args[1] : null;

            long startTime = System.currentTimeMillis();
            Sound sound = new Sound(name, set);
            sound.playFrontend();
            script.enqueueTask(e -> {
                if (System.currentTimeMillis() - startTime >= 5000) {
                    if (!sound.isFinished()) {
                        sound.stop();
                    }
                    return true;
                }
                return false;
            });
        } else {
            throw new WrongUsageException(this, "<name> <set>");
        }
    }
}

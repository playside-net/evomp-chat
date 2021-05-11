package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.runtime.Runtime;
import mp.evolution.script.Script;

public class CommandReload extends Command {
    public CommandReload(Script script) {
        super(script, "reload");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        message("Reloading...");
        Runtime.INSTANCE.reload();
    }
}

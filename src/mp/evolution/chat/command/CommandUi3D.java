package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.script.Script;

public class CommandUi3D extends Command {
    public CommandUi3D(Script script) {
        super(script, "ui3d");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        ((Chat) script).push("~y~UI3D is ~w~" + (script.invokeBoolean(0xD3A10FC7FD8D98CDL) ? "" : "not ") + "available");
        ((Chat) script).push("~y~Pushed preset CELEBRATION_WINNER: ~w~" + script.invokeBoolean(0xF1CEA8A4198D8E9AL, "CELEBRATION_WINNER"));
    }
}

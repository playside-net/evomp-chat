package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.streaming.Interior;
import mp.evolution.game.streaming.InteriorInfo;
import mp.evolution.script.Script;

public class CommandInterior extends Command {
    public CommandInterior(Script script) {
        super(script, "interior");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 0) {
            Interior i = Interior.fromPos(script, player.getPosition());
            InteriorInfo info = i.getInfo();
            ((Chat) script).push("~y~Interior #" + i.handle() + ": ~w~" + info.pos + " " + info.hash);
        } else {
            throw new WrongUsageException(this, "");
        }
    }
}
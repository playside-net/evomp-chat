package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.audio.Alarm;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.script.Script;

public class CommandAlarm extends Command {
    public CommandAlarm(Script script) {
        super(script, "alarm");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            String name = args[0];
            boolean enable = parseBoolean(args[1]);
            Alarm alarm = new Alarm(name);
            if (enable) {
                alarm.prepare();
                alarm.start(false);
                message("Started alarm ~y~" + name);
            } else {
                alarm.stop();
                message("Stopped alarm ~y~" + name);
            }
        } else {
            throw new WrongUsageException(this, "<alarm> <enable>");
        }
    }
}

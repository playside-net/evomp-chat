package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.controls.Control;
import mp.evolution.game.controls.ControlGroup;
import mp.evolution.game.controls.Controls;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.script.Script;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class CommandControl extends Command {
    protected final Controls controls = new Controls(script);

    private final Set<Control> disabled = new HashSet<>();

    public CommandControl(Script script) {
        super(script, "control");
        script.enqueueTask(p -> {
            for (Control c : disabled) {
                controls.disableAction(ControlGroup.MOVE, c, true);
            }
            return false;
        });
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            Control control = Control.valueOf(args[0].toUpperCase(Locale.ROOT));
            boolean enabled = parseBoolean(args[1]);
            if (enabled) {
                disabled.remove(control);
                message("~y~Enabled ~w~" + control.name());
            } else {
                disabled.add(control);
                message("~y~Disabled ~w~" + control.name());
            }
        } else if (args.length == 0) {
            throw new WrongUsageException(this, "<name> <enabled>");
        }
    }
}

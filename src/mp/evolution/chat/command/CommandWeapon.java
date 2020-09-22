package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.chat.command.exception.WrongUsageException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.script.Script;

public class CommandWeapon extends Command {
    public CommandWeapon(Script script) {
        super(script, "weapon");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length == 2) {
            int weapon = Script.joaat("weapon_" + args[0]);
            int ammo = parseInt(args[1]);
            player.giveWeapon(weapon, ammo, false, false);
        } else {
            throw new WrongUsageException(this, "<name> <ammo>");
        }
    }
}

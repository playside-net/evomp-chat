package mp.evolution.chat.command;

import mp.evolution.chat.Chat;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.invoke.MutableInt;
import mp.evolution.script.Script;

import java.nio.ByteBuffer;

public class CommandStats extends Command {
    public CommandStats(Script script) {
        super(script, "stats");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        Chat chat = (Chat) this.script;
        MutableInt hash = new MutableInt(0);
        if (player.invokeBoolean(0x3A87E44BB9A01D54L, hash, true)) {
            ByteBuffer stats = ByteBuffer.allocateDirect(5 * 8);
            int h = hash.get();
            chat.push("Weapon hash: ~g~" + Integer.toString(h, 16));
            if (script.invokeBoolean(0xD92C739EE34C9EBAL, h, stats)) {
                int damage = stats.get();
                stats.position(stats.position() + 0x7);
                int speed = stats.get();
                stats.position(stats.position() + 0x7);
                int capacity = stats.get();
                stats.position(stats.position() + 0x7);
                int accuracy = stats.get();
                stats.position(stats.position() + 0x7);
                int range = stats.get();
                stats.position(stats.position() + 0x7);
                chat.push("Damage: ~y~" + damage);
                chat.push("Speed: ~y~" + speed);
                chat.push("Capacity: ~y~" + capacity);
                chat.push("Accuracy: ~y~" + accuracy);
                chat.push("Range: ~y~" + range);
            } else {
                chat.push("~r~Failed to get weapon stats");
            }
        } else {
            chat.push("~r~Failed to get weapon hash");
        }
    }
}

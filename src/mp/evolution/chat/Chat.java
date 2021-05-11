package mp.evolution.chat;

import mp.evolution.chat.command.*;
import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.game.controls.Control;
import mp.evolution.game.controls.ControlGroup;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.game.ui.UI;
import mp.evolution.game.ui.UI.Font;
import mp.evolution.math.RGBA;
import mp.evolution.script.Script;
import mp.evolution.script.event.ScriptEvent;
import mp.evolution.script.event.ScriptEventKeyboardKey;
import mp.evolution.script.event.ScriptEventKeyboardKey.KeyCode;
import mp.evolution.script.event.ScriptEventUserInput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Chat extends Script {
    public static final Font FONT = Font.CHALET_LONDON;
    public static final float CONSOLE_WIDTH = UI.BASE_WIDTH;
    public static final float CONSOLE_HEIGHT = UI.BASE_HEIGHT / 3;
    public static final float INPUT_HEIGHT = 20;
    public static final int LINES_PER_PAGE = 16;
    public static final RGBA PAGE_COLOR = RGBA.WHITE;
    public static final RGBA OUTPUT_COLOR = RGBA.WHITE;
    public static final RGBA BACKGROUND_COLOR = new RGBA(0, 0, 0, 127);

    public static AtomicBoolean OPEN = new AtomicBoolean(false);
    public static AtomicBoolean INPUT_LOCKED = new AtomicBoolean(false);
    public static AtomicLong LAST_CLOSED = new AtomicLong(System.currentTimeMillis());

    private final List<String> lineHistory = new ArrayList<>();
    private final TextInput input = new TextInput(this, "", CONSOLE_WIDTH, INPUT_HEIGHT, FONT, true) {
        @Override
        public void entered(String input) {
            if (input.startsWith("/")) {
                System.out.println("Chat command: " + input);
                try {
                    commandManager.execute(Ped.local(Chat.this), input.substring(1));
                } catch (CommandException e) {
                    push("~r~" + e.getMessage());
                }
            } else {
                System.out.println("Chat message: " + input);
            }
        }
    };

    private final CommandManager commandManager = new CommandManager(this);

    public Chat() {
        registerCommand(new CommandTeleport(this));
        registerCommand(new CommandVehicle(this));
        registerCommand(new CommandTrain(this));
        registerCommand(new CommandMod(this));
        registerCommand(new CommandZone(this));
        registerCommand(new CommandTime(this));
        registerCommand(new CommandWeapon(this));
        registerCommand(new CommandMaterial(this));
        registerCommand(new CommandRGB(this));
        registerCommand(new CommandModel(this));
        registerCommand(new CommandFuel(this));
        registerCommand(new CommandPed(this));
        registerCommand(new CommandControl(this));
        registerCommand(new CommandOil(this));
        registerCommand(new CommandStats(this));
        registerCommand(new CommandMusicEvent(this));
        registerCommand(new CommandProp(this));
        registerCommand(new CommandSeat(this));
        registerCommand(new CommandSound(this));
        registerCommand(new CommandUi3D(this));
        registerCommand(new CommandInterior(this));
        registerCommand(new CommandInteriorColor(this));
        registerCommand(new CommandCam(this));
        registerCommand(new CommandLivery(this));
        registerCommand(new CommandRoofLivery(this));
        registerCommand(new CommandColor(this));
        registerCommand(new CommandAlarm(this));
        registerCommand(new CommandReload(this));
    }

    @Override
    public void frame() {
        if (OPEN.get()) {
            lockControls();
            draw();
        } else if (LAST_CLOSED.get() > System.currentTimeMillis()) {
            lockControls();
        }
    }

    @Override
    public boolean event(ScriptEvent event) {
        if (event instanceof ScriptEventUserInput) {
            if (event instanceof ScriptEventKeyboardKey) {
                ScriptEventKeyboardKey e = (ScriptEventKeyboardKey) event;
                if (!e.isUp) {
                    if (e.key == KeyCode.ESCAPE && OPEN.get()) {
                        enqueueTask(self -> {
                            OPEN.set(false);
                            input.reset();
                            LAST_CLOSED.set(System.currentTimeMillis() + 200);
                            lockControls();
                            return true;
                        });
                        return true;
                    } else if (e.key == KeyCode.KEY_T && !OPEN.get()) {
                        INPUT_LOCKED.set(true);
                        enqueueTask(self -> {
                            OPEN.set(true);
                            return true;
                        });
                        return true;
                    } else if (e.key == KeyCode.OEM_2 && !OPEN.get()) {
                        enqueueTask(self -> {
                            OPEN.set(true);
                            return true;
                        });
                    } else if (e.key == KeyCode.F3) {
                        push("Coord: " + invokeVector3(0x5BFF36D6ED83E0AEL));
                    }
                } else if (e.key == KeyCode.KEY_T && INPUT_LOCKED.get()) {
                    enqueueTask(self -> {
                        INPUT_LOCKED.set(false);
                        return true;
                    });
                }
            }
            if (OPEN.get() && !INPUT_LOCKED.get()) {
                input.input((ScriptEventUserInput) event);
            }
        }
        return false;
    }

    public void push(String message) {
        lineHistory.add(message);
    }

    public void registerCommand(Command command) {
        commandManager.register(command);
    }

    private void lockControls() {
        controls.disableAllActions(ControlGroup.MOVE);
        controls.enableAction(ControlGroup.MOVE, Control.LOOK_LEFT_RIGHT, true);
        controls.enableAction(ControlGroup.MOVE, Control.LOOK_UP_DOWN, true);
        controls.enableAction(ControlGroup.MOVE, Control.LOOK_UP_ONLY, true);
        controls.enableAction(ControlGroup.MOVE, Control.LOOK_DOWN_ONLY, true);
        controls.enableAction(ControlGroup.MOVE, Control.LOOK_LEFT_ONLY, true);
        controls.enableAction(ControlGroup.MOVE, Control.LOOK_RIGHT_ONLY, true);
    }

    private void draw() {
        float scaleX = 0.35F;
        float scaleY = 0.35F;
        // Draw background
        ui.drawRect(0, 0, CONSOLE_WIDTH, CONSOLE_HEIGHT, BACKGROUND_COLOR);
        int totalPages = Math.max(1, (lineHistory.size() + LINES_PER_PAGE - 1) / LINES_PER_PAGE);
        int currentPage = 1;
        ui.drawText("Page " + currentPage + "/" + totalPages, 5, CONSOLE_HEIGHT + INPUT_HEIGHT, PAGE_COLOR, FONT, scaleX, scaleY);

        input.draw(0, CONSOLE_HEIGHT);

        // Draw console history text
        int historyOffset = lineHistory.size() - LINES_PER_PAGE * currentPage;
        int historyLength = historyOffset + LINES_PER_PAGE;
        for (int i = Math.max(0, historyOffset); i < historyLength; i++) {
            ui.drawText(lineHistory.get(i), 2, (i  - historyOffset) * 14, OUTPUT_COLOR, FONT, scaleX, scaleY);
        }
    }
}

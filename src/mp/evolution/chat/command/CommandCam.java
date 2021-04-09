package mp.evolution.chat.command;

import mp.evolution.chat.command.exception.CommandException;
import mp.evolution.game.camera.Camera;
import mp.evolution.game.camera.CameraShake;
import mp.evolution.game.camera.CameraType;
import mp.evolution.game.entity.ped.Ped;
import mp.evolution.math.Vector3;
import mp.evolution.script.Script;

import java.util.Locale;

public class CommandCam extends Command {
    private Camera cam;

    public CommandCam(Script script) {
        super(script, "cam");
    }

    @Override
    public void execute(Ped player, String[] args) throws CommandException {
        if (args.length > 0) {
            switch (args[0]) {
                case "n":
                    Vector3 pos = Camera.Gameplay.getPosition(script);
                    Vector3 rot = Camera.Gameplay.getRotation(script, 2);
                    if (cam == null) {
                        cam = new Camera(script, CameraType.DEFAULT_SPLINE, pos, rot, 50);
                    }
                    cam.addSplineNode(pos, rot, 5000, 0, 0);
                    message("Node: ~y~" + pos + " -> " + rot);
                    break;
                case "r":
                    if (cam != null) {
                        cam.setSplinePhase(0);
                        cam.setActive(true);
                        Camera.renderScripted(script, true);
                    }
                    break;
                case "p":
                    if (cam != null) {
                        if (args.length > 1) {
                            float p = parseFloat(args[1]);
                            if (cam != null) {
                                cam.setSplinePhase(p);
                            }
                        } else {
                            message("Spline phase: ~y~" + cam.getSplinePhase() + " n " + cam.getSplineNodePhase());
                        }
                    }
                case "d": {
                    if (cam != null) {
                        cam.setActive(false);
                        cam.destroy();
                        cam = null;
                        Camera.renderScripted(script, false);
                    }
                }
                case "ss": {
                    if (cam != null) {
                        if (args.length == 2) {
                            int style = parseInt(args[1]);
                            cam.setSplineSmoothStyle(style);
                        }
                    }
                }
                case "shake": {
                    if (args.length == 3) {
                        CameraShake shake = CameraShake.valueOf(args[1].toUpperCase(Locale.ROOT));
                        float a = parseFloat(args[2]);
                        Camera.Gameplay.shake(script, shake, a);
                    }
                }
                case "stopshaking": {
                    boolean instant = args.length > 1;
                    Camera.Gameplay.stopShaking(script, instant);
                }
            }
        }
    }
}

package mp.evolution.chat;

import mp.evolution.game.math.RGBA;
import mp.evolution.game.ui.UI;
import mp.evolution.script.Script;
import mp.evolution.script.event.ScriptEventKeyboardChar;
import mp.evolution.script.event.ScriptEventKeyboardKey;
import mp.evolution.script.event.ScriptEventUserInput;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class TextInput extends UI {
    public static final RGBA INPUT_COLOR = RGBA.WHITE;
    public static final RGBA INPUT_COLOR_BUSY = new RGBA(81, 81, 81, 255);
    public static final RGBA PREFIX_COLOR = new RGBA(52, 152, 219, 255);
    public static final RGBA ALT_BACKGROUND_COLOR = new RGBA(52, 73, 94, 127);
    public static final RGBA SELECTION_COLOR = new RGBA(0, 0, 255, 127);
    public static final RGBA CURSOR_COLOR = new RGBA(255, 255, 255, 127);

    private int selectionStart = 0, selectionEnd = 0;
    private String input;
    private int historyPos = 0;
    private final List<String> history = new ArrayList<>();
    private long lastSelectionChanged = System.currentTimeMillis();
    public final float width;
    public final float height;
    public final Font font;
    public final boolean enableHistory;

    public TextInput(Script script, String input, float width, float height, Font font, boolean enableHistory) {
        super(script);
        this.input = input;
        this.width = width;
        this.height = height;
        this.font = font;
        this.enableHistory = enableHistory;
    }

    public void draw(float startX, float startY) {
        long now = System.currentTimeMillis();
        float scaleX = 0.35F;
        float scaleY = 0.35F;
        int start = selectionStart;
        int end = selectionEnd;
        // Draw input field
        drawRect(startX, startY, width, height, ALT_BACKGROUND_COLOR);
        drawRect(startX, startY + width, 80, height, ALT_BACKGROUND_COLOR);
        // Draw input prefix
        drawText(">", startX, startY, PREFIX_COLOR, font, scaleX, scaleY);
        // Draw input text
        drawText(getInput().replace("~", "\\~"), startX + 25, startY, INPUT_COLOR, font, scaleX, scaleY);
        // Draw page information
        if (start == end) {
            if ((now - lastSelectionChanged) % 1000 < 500) {
                String prefix = getInput().substring(0, start);
                float x = getTextWidth(prefix.replace("~", "\\~"), font, scaleX, scaleY) * width;
                x = prefix.isEmpty() ? x - 0.5F : x - 4;
                drawRect(25 + startX + x, startY + 2, 1.5F, height - 4, CURSOR_COLOR);
            }
        } else {
            int from = min(start, end);
            int to = max(start, end);
            String prefix = getInput().substring(0, from);
            float x = getTextWidth(prefix.replace("~", "\\~"), font, scaleX, scaleY) * this.width;
            String selected = getInput().substring(from, to);
            float width = getTextWidth(selected.replace("~", "\\~"), font, scaleX, scaleY) * this.width;
            x = prefix.isEmpty() ? x - 0.5F : x - 4;
            drawRect(25 + startX + x, startY + 2, width, height - 4, SELECTION_COLOR);
        }
    }

    public void input(ScriptEventUserInput event) {
        if (event instanceof ScriptEventKeyboardKey) {
            ScriptEventKeyboardKey e = (ScriptEventKeyboardKey) event;
            if (!e.isUp) {
                switch (e.key) {
                    case LEFT:
                        if (e.control) {
                            if (e.shift) {
                                selectionEnd = 0;
                            } else {
                                selectionStart = selectionEnd = 0;
                            }
                        } else if (e.shift) {
                            if (selectionEnd > 0) {
                                selectionEnd -= 1;
                            }
                        } else {
                            int to = min(selectionStart, selectionEnd);
                            if (to > 0) {
                                selectionStart = selectionEnd = to - 1;
                            } else {
                                selectionStart = selectionEnd = to;
                            }
                        }
                        lastSelectionChanged = System.currentTimeMillis();
                        break;
                    case RIGHT: {
                        int len = len();
                        if (e.control) {
                            if (e.shift) {
                                selectionEnd = len;
                            } else {
                                selectionStart = selectionEnd = len;
                            }
                        } else if (e.shift) {
                            if (selectionEnd < len) {
                                selectionEnd += 1;
                            }
                        } else {
                            int to = min(selectionStart, selectionEnd);
                            if (to < len) {
                                selectionStart = selectionEnd = to + 1;
                            } else {
                                selectionStart = selectionEnd = to;
                            }
                        }
                        lastSelectionChanged = System.currentTimeMillis();
                        break;
                    }
                    case HOME: {
                        int len = len();
                        if (e.shift) {
                            selectionEnd = 0;
                        } else {
                            selectionStart = selectionEnd = 0;
                        }
                        lastSelectionChanged = System.currentTimeMillis();
                        break;
                    }
                    case END: {
                        int len = len();
                        if (e.shift) {
                            selectionEnd = len;
                        } else {
                            selectionStart = selectionEnd = len;
                        }
                        lastSelectionChanged = System.currentTimeMillis();
                        break;
                    }
                    case UP: {
                        if (enableHistory && historyPos < history.size()) {
                            historyPos += 1;
                            int len = len();
                            selectionStart = selectionEnd = len;
                            lastSelectionChanged = System.currentTimeMillis();
                        }
                        break;
                    }
                    case DOWN: {
                        if (enableHistory && historyPos > 0) {
                            historyPos -= 1;
                            int len = len();
                            selectionStart = selectionEnd = len;
                            lastSelectionChanged = System.currentTimeMillis();
                        }
                        break;
                    }
                    case RETURN: {
                        if (historyPos == 0) {
                            if (!input.isEmpty()) {
                                String input = this.input;
                                this.input = "";
                                selectionStart = selectionEnd = 0;
                                if (enableHistory) {
                                    history.add(input);
                                }
                                entered(input);
                            }
                        } else {
                            String input = history.get(history.size() - historyPos);
                            reset();
                            entered(input);
                        }
                        break;
                    }
                    case KEY_A: {
                        if (e.control) {
                            int len = len();
                            selectionStart = 0;
                            selectionEnd = len;
                            lastSelectionChanged = System.currentTimeMillis();
                        }
                        break;
                    }
                    case KEY_C: {
                        if (e.control) {
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            String text = getInput().substring(selectionStart, selectionEnd);
                            if (!text.isEmpty()) {
                                clipboard.setContents(new StringSelection(text), null);
                            }
                        }
                        break;
                    }
                    case KEY_X: {
                        if (e.control) {
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            int from = min(selectionStart, selectionEnd);
                            int to = max(selectionStart, selectionEnd);
                            String text = getInput().substring(from, to);
                            if (!text.isEmpty()) {
                                clipboard.setContents(new StringSelection(text), null);
                                replaceChars(selectionStart, selectionEnd, "");
                            }
                        }
                        break;
                    }
                    case KEY_V: {
                        if (e.control) {
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                                String text;
                                try {
                                    text = (String) clipboard.getData(DataFlavor.stringFlavor);
                                } catch (UnsupportedFlavorException | IOException ex) {
                                    ex.printStackTrace();
                                    return;
                                }
                                if (!text.isEmpty()) {
                                    replaceChars(selectionStart, selectionEnd, text);
                                    int pos = min(selectionStart, selectionEnd) + text.length();
                                    selectionStart = selectionEnd = pos;
                                    lastSelectionChanged = System.currentTimeMillis();
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } else if (event instanceof ScriptEventKeyboardChar) {
            ScriptEventKeyboardChar e = (ScriptEventKeyboardChar) event;
            switch (e.symbol) {
                case '\u0008': {
                    eraseLeft();
                    break;
                }
                case '\u007F': {
                    eraseRight();
                    break;
                }
                default: {
                    if (!Character.isISOControl(e.symbol)) {
                        enterChar(e.symbol);
                    }
                    break;
                }
            }
        }
    }

    private void eraseLeft() {
        if (selectionStart == selectionEnd && selectionStart > 0) {
            replaceChars(selectionStart - 1, selectionStart, "");
        } else {
            replaceChars(selectionStart, selectionEnd, "");
        }
    }

    private void eraseRight() {
        int len = len();
        if (selectionStart == selectionEnd && selectionEnd < len) {
            replaceChars(selectionEnd, selectionEnd + 1, "");
        } else {
            replaceChars(selectionStart, selectionEnd, "");
        }
    }

    private void enterChar(char c) {
        int start = selectionStart;
        int end = selectionEnd;
        replaceChars(start, end, String.valueOf(c));
        selectionStart = selectionEnd = min(start, end) + 1;
    }

    private void replaceChars(int start, int end, String replacement) {
        int from = min(start, end);
        int to = max(start, end);
        String oldInput = getInput();
        String newInput = oldInput.substring(0, from) + replacement + oldInput.substring(to);
        if (historyPos != 0 && !oldInput.equals(newInput)) {
            historyPos = 0;
        }
        setInput(newInput);
        selectionStart = selectionEnd = from;
        lastSelectionChanged = System.currentTimeMillis();
    }

    public void reset() {
        selectionStart = selectionEnd = 0;
        lastSelectionChanged = System.currentTimeMillis();
        input = "";
        historyPos = 0;
    }

    public int len() {
        return getInput().length();
    }

    public String getInput() {
        return historyPos == 0 ? input : history.get(history.size() - historyPos);
    }

    public void setInput(String text) {
        if (historyPos == 0) {
            input = text;
        } else {
            history.set(history.size() - historyPos, text);
        }
    }

    public abstract void entered(String input);
}

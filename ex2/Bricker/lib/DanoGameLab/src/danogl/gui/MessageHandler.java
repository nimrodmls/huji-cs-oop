package danogl.gui;

import javax.swing.*;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * Implementation of a logging mechanism: showing messages with different levels
 * of emergency and different output methods.
 * <br>See {@link MsgLevel} for the different possible levels of urgency,
 * and {@link OutputMode} for the possible behaviors for these levels.
 * <br>This class allows customizing which level results in which output behavior,
 * and of course logging a given message with a given MsgLevel or outputModes.
 * @author Dan Nirel
 */
public class MessageHandler {
    private WindowController windowController;
    private Map<MsgLevel, EnumSet<OutputMode>> outputModesOfMsgLevel =
            new EnumMap<MsgLevel, EnumSet<OutputMode>>(Map.of(
                    MsgLevel.DEBUG,         EnumSet.of(OutputMode.STANDARD_OUTPUT),
                    MsgLevel.WARNING,       EnumSet.of(OutputMode.STANDARD_ERROR),
                    MsgLevel.INFO,          EnumSet.of(OutputMode.STANDARD_OUTPUT),
                    MsgLevel.ERROR,         EnumSet.of(OutputMode.MSG_BOX),
                    MsgLevel.CRITICAL,      EnumSet.of(OutputMode.MSG_BOX, OutputMode.EXIT),
                    MsgLevel.USER_LEVEL_1,  EnumSet.of(OutputMode.STANDARD_OUTPUT),
                    MsgLevel.USER_LEVEL_2,  EnumSet.of(OutputMode.STANDARD_OUTPUT)
            ));

    public MessageHandler(WindowController windowController) {

        this.windowController = windowController;
    }

    /**
     * Retrieve the current output modes for the specified message level.
     * This output modes will determine how a message sent to {@link #showMessage(String, MsgLevel)}
     * will be displayed
     */
    public EnumSet<OutputMode> outputModesOfMsgLevel(MsgLevel msgLevel) {
        return outputModesOfMsgLevel.get(msgLevel);
    }

    /**
     * Alter the current output modes for the specified message level.
     * This output mode will determine how a message sent to {@link #showMessage(String, MsgLevel)}
     * will be displayed
     * @return the previous output modes for the specified level
     */
    public EnumSet<OutputMode> setOutputModeOfMsgLevel(MsgLevel msgLevel, EnumSet<OutputMode> outputModes) {
        return outputModesOfMsgLevel.replace(msgLevel, outputModes);
    }

    /**
     * Output a message at a specified level. This is the recommended method
     * for this task. The output modes of the message will be determined by
     * the current {@link #outputModesOfMsgLevel(MsgLevel)} and can be changed using
     * {@link #setOutputModeOfMsgLevel(MsgLevel, EnumSet)}.
     * @param msg the message to be displayed
     * @param msgLevel the level of this message (informational, error, etc.)
     */
    public void showMessage(String msg, MsgLevel msgLevel) {
        showMessage(msg, outputModesOfMsgLevel.get(msgLevel));
    }

    /**
     * Output a message at specified output modes. A more general way of doing this
     * would be using the overload {@link #showMessage(String, MsgLevel)} which
     * would indirectly determine the output modes using the default (alterable)
     * mode for the specified msg level. However, this overload may be simpler to use
     * in simpler cases.
     * @param msg the message to be displayed
     * @param outputModes all desired output modes
     */
    public void showMessage(String msg, EnumSet<OutputMode> outputModes) {
        if(outputModes.contains(OutputMode.STANDARD_OUTPUT))
            System.out.println(msg);
        if(outputModes.contains(OutputMode.STANDARD_ERROR))
            System.err.println(msg);
        if(outputModes.contains(OutputMode.MSG_BOX))
            JOptionPane.showMessageDialog(parentWindow(), msg);
        if(outputModes.contains(OutputMode.EXIT))
            windowController.closeWindow();
    }

    /**
     * Output a message at a single specified output mode. A more general way of doing this
     * would be using the overload {@link #showMessage(String, MsgLevel)} which
     * would indirectly determine the output modes using the default (alterable)
     * mode for the specified msg level. This overload is simpler to use
     * in simpler cases.
     * @param msg the message to be displayed
     * @param mode the desired output mode
     */
    public void showMessage(String msg, OutputMode mode) {
        showMessage(msg, EnumSet.of(mode));
    }

    /**Open a dialog window that shows the provided prompt and then asks
     * for yes or no. While the dialog is open, the game is paused.
     * @return true if the user chose yes, false if they chose no.
     */
    public boolean openYesNoDialog(String msg) {
        int res = JOptionPane.showOptionDialog(
                parentWindow(), msg, "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        return res == 0;
    }

    private java.awt.Component parentWindow() {
        return (windowController instanceof GameGUIComponent)?
                (GameGUIComponent) windowController : null;
    }
}

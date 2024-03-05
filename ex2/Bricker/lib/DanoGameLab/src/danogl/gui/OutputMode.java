package danogl.gui;

/**
 * Specifying the different output methods available to messages.
 * <br>To create a set of such modes as the behavior to some message level,
 * use EnumSet.of, for example EnumSet.of(OutputMode.STANDARD_OUTPUT, OutputMode.EXIT) .
 * @see MessageHandler
 * @author Dan Nirel
 */
public enum OutputMode {
        NONE,
        STANDARD_OUTPUT,
        STANDARD_ERROR,
        MSG_BOX,
        EXIT
}

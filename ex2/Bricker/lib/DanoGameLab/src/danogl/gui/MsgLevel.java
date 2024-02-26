package danogl.gui;

/**
 * Specifying the different urgency levels available to a message.
 * You can specify the behavior for each one (including the user-levels) in
 * {@link MessageHandler}.
 * @author Dan Nirel
 */
public enum MsgLevel {
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    CRITICAL,
    USER_LEVEL_1,
    USER_LEVEL_2
}

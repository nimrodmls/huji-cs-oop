package exceptions;

/**
 * An exception that is thrown when a shell command was not executed successfully.
 * @author Nimrod M.
 */
public class ShellCommandException extends Exception {

    /**
     * @param message The message to be displayed when the exception is thrown.
     */
    public ShellCommandException(String message) {
        super(message);
    }
}

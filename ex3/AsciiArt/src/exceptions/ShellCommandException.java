package exceptions;

public class ShellCommandException extends Exception {

    /**
     * @param message The message to be displayed when the exception is thrown.
     */
    public ShellCommandException(String message) {
        super(message);
    }
}

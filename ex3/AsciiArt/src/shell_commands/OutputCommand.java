package shell_commands;

import ascii_art.OutputController;
import ascii_art.OutputMethod;
import exceptions.ShellCommandException;

/**
 * A command that changes the output method of the ASCII Art to the user.
 * @author Nimrod M.
 */
public class OutputCommand implements ShellCommand {

    private static final String INVALID_ARGUMENT_MESSAGE =
            "Did not change output method due to incorrect format.";

    private final OutputController outputController;

    /**
     * Constructs a new OutputCommand.
     * Controls the output method of the ASCII Art using the given output controller.
     * @param outputController The output controller to be used for the ASCII Art
     */
    public OutputCommand(OutputController outputController) {

        this.outputController = outputController;
    }

    /**
     * Executes the command, changing the output method of the ASCII Art to the user.
     * (without executing the algorithm itself)
     * @param arguments The arguments for the command - "html" or "console"
     * @throws ShellCommandException If the command was not executed successfully
     */
    @Override
    public void execute(String[] arguments) throws ShellCommandException {
        if (1 != arguments.length) {
            throw new ShellCommandException(INVALID_ARGUMENT_MESSAGE);
        }

        String outputMethod = arguments[0];
        switch (outputMethod) {
            case "html":
                outputController.setOutputMethod(OutputMethod.HTML);
                break;
            case "console":
                outputController.setOutputMethod(OutputMethod.CONSOLE);
                break;
            default:
                throw new ShellCommandException(INVALID_ARGUMENT_MESSAGE);
        }
    }
}

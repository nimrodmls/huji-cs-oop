package shell_commands;

import ascii_art.OutputController;
import ascii_art.OutputMethod;
import exceptions.InvalidCommandFormatException;

public class OutputCommand implements ShellCommand {

    private static final String INVALID_ARGUMENT_MESSAGE =
            "Did not change output method due to incorrect format.";

    private final OutputController outputController;

    public OutputCommand(OutputController outputController) {

        this.outputController = outputController;
    }

    @Override
    public void execute(String[] arguments) throws InvalidCommandFormatException {
        if (1 != arguments.length) {
            throw new InvalidCommandFormatException(INVALID_ARGUMENT_MESSAGE);
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
                throw new InvalidCommandFormatException(INVALID_ARGUMENT_MESSAGE);
        }
    }
}

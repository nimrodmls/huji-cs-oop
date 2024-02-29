package shell_commands;

import ascii_art.OutputController;
import ascii_art.OutputMethod;
import exceptions.ShellCommandException;

public class OutputCommand implements ShellCommand {

    private static final String INVALID_ARGUMENT_MESSAGE =
            "Did not change output method due to incorrect format.";

    private final OutputController outputController;

    public OutputCommand(OutputController outputController) {

        this.outputController = outputController;
    }

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

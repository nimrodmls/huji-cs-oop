package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.OutputController;
import exceptions.ShellCommandException;

/**
 * A command that outputs the ASCII Art to the user in the selected form of output.
 * @author Nimrod M.
 */
public class AsciiArtCommand implements ShellCommand {

    private final AsciiArtAlgorithm asciiArtAlgorithm;
    private final OutputController outputController;

    /**
     * Constructs a new AsciiArtCommand object with the given algorithm and output controller.
     * @param asciiArtAlgorithm The algorithm to be used for the ASCII Art
     * @param outputController The output controller to be used for the ASCII Art
     */
    public AsciiArtCommand(AsciiArtAlgorithm asciiArtAlgorithm, OutputController outputController) {

        this.asciiArtAlgorithm = asciiArtAlgorithm;
        this.outputController = outputController;
    }

    /**
     * Executes the command, outputting the ASCII Art to the user in the selected form of output.
     * @param arguments The arguments for the command - not used
     * @throws ShellCommandException If the command was not executed successfully
     */
    @Override
    public void execute(String[] arguments) throws ShellCommandException {
        if (0 == asciiArtAlgorithm.getCharset().length) {
            throw new ShellCommandException("Did not execute. Charset is empty.");
        }

        char[][] asciiArt = asciiArtAlgorithm.run();
        outputController.toOutput(asciiArt);
    }
}

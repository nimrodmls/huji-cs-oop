package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.OutputController;
import exceptions.InvalidCommandFormatException;

public class AsciiArtCommand implements ShellCommand {

    private final AsciiArtAlgorithm asciiArtAlgorithm;
    private final OutputController outputController;

    public AsciiArtCommand(AsciiArtAlgorithm asciiArtAlgorithm, OutputController outputController) {

        this.asciiArtAlgorithm = asciiArtAlgorithm;
        this.outputController = outputController;
    }

        @Override
        public void execute(String[] arguments) throws InvalidCommandFormatException {
            if (0 == asciiArtAlgorithm.getCharset().length) {
                throw new InvalidCommandFormatException("Did not execute. Charset is empty.");
            }

            char[][] asciiArt = asciiArtAlgorithm.run();
            outputController.toOutput(asciiArt);
        }
}

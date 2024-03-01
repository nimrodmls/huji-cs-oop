package shell_commands;

import ascii_art.AsciiArtAlgorithm;

/**
 * A command that outputs the charset of the ASCII Art to the user.
 * @author Nimrod M.
 */
public class CharDisplayCommand implements ShellCommand {

    private final AsciiArtAlgorithm asciiArtAlgorithm;

    /**
     * Constructs a new CharDisplayCommand object with the given algorithm.
     * @param asciiArtAlgorithm The algorithm to be used for the ASCII Art
     */
    public CharDisplayCommand(AsciiArtAlgorithm asciiArtAlgorithm) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
    }

    /**
     * Executes the command, outputting the charset of the ASCII Art to the user (stdout).
     * @param arguments The arguments for the command - not used
     */
    @Override
    public void execute(String[] arguments) {
        char[] charset = asciiArtAlgorithm.getCharset();
        for (char c : charset) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
}

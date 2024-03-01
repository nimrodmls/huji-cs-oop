package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import exceptions.ShellCommandException;
import image.Image;

/**
 * A command that changes the resolution of the ASCII Art to the user.
 * @author Nimrod M.
 */
public class ResolutionModifyCommand implements ShellCommand {

    private static final String INVALID_ARGUMENT_MESSAGE =
            "Did not change resolution due to incorrect format.";
    private static final String CRITICAL_RESOLUTION_MESSAGE =
            "Did not change resolution due to exceeding boundaries.";
    private static final String RESOLUTION_CHANGED_MESSAGE = "Resolution set to %d.\n";
    private static final String TOKEN_UP = "up";
    private static final String TOKEN_DOWN = "down";

    private final AsciiArtAlgorithm asciiArtAlgorithm;

    /**
     * Constructs a new ResolutionModifyCommand.
     * Controls the resolution of the ASCII Art using the given algorithm.
     * @param asciiArtAlgorithm The algorithm to be used for the ASCII Art
     */
    public ResolutionModifyCommand(AsciiArtAlgorithm asciiArtAlgorithm) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
    }

    /**
     * Executes the command, changing the resolution of the ASCII Art to the user.
     * Only changes the resolution if it is within the boundaries.
     * @param arguments The arguments for the command - "up" or "down"
     * @throws ShellCommandException If the command was not executed successfully
     */
    @Override
    public void execute(String[] arguments) throws ShellCommandException {
        if (1 != arguments.length) {
            throw new ShellCommandException(INVALID_ARGUMENT_MESSAGE);
        }

        String token = arguments[0];
        int newResolution = asciiArtAlgorithm.getResolution();
        switch (token) {
            case TOKEN_UP:
                newResolution *= 2;
                break;
            case TOKEN_DOWN:
                newResolution /= 2;
                break;
            default:
                throw new ShellCommandException(INVALID_ARGUMENT_MESSAGE);
        }

        if (!validateResolution(newResolution)) {
            throw new ShellCommandException(CRITICAL_RESOLUTION_MESSAGE);
        }
        asciiArtAlgorithm.setResolution(newResolution);
        System.out.printf(RESOLUTION_CHANGED_MESSAGE, newResolution);
    }

    private boolean validateResolution(int resolution) {
        Image image = asciiArtAlgorithm.getImage();
        int minCharsInRow = Math.max(
                1, image.getWidth() / image.getHeight());
        int maxCharsInRow = image.getWidth();
        return (resolution <= maxCharsInRow) && (resolution >= minCharsInRow);
    }
}

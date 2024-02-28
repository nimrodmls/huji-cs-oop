package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import exceptions.InvalidCommandFormatException;
import image.Image;

public class ResolutionModifyCommand implements ShellCommand {

    private static final String INVALID_ARGUMENT_MESSAGE = "Did not change resolution due to incorrect format.";
    private static final String CRITICAL_RESOLUTION_MESSAGE = "Did not change resolution due to exceeding boundaries.";
    private static final String RESOLUTION_CHANGED_MESSAGE = "Resolution set to %d.\n";
    private static final String TOKEN_UP = "up";
    private static final String TOKEN_DOWN = "down";

    private int resolution;
    private final AsciiArtAlgorithm asciiArtAlgorithm;

    public ResolutionModifyCommand(int defaultResolution, AsciiArtAlgorithm asciiArtAlgorithm) {
        this.resolution = defaultResolution;
        this.asciiArtAlgorithm = asciiArtAlgorithm;
    }

    @Override
    public void execute(String[] arguments) throws InvalidCommandFormatException {
        if (1 != arguments.length) {
            throw new InvalidCommandFormatException(INVALID_ARGUMENT_MESSAGE);
        }

        String token = arguments[0];
        int newResolution = resolution;
        switch (token) {
            case TOKEN_UP:
                newResolution *= 2;
                break;
            case TOKEN_DOWN:
                newResolution /= 2;
                break;
            default:
                throw new InvalidCommandFormatException(INVALID_ARGUMENT_MESSAGE);
        }

        if (!validateResolution(newResolution)) {
            throw new InvalidCommandFormatException(CRITICAL_RESOLUTION_MESSAGE);
        }
        resolution = newResolution;
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

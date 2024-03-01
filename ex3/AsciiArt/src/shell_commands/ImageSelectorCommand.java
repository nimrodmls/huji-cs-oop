package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import exceptions.ShellCommandException;
import image.Image;
import image.SimpleImage;

import java.io.IOException;

/**
 * A command that selects an image for the ASCII Art algorithm to use.
 * @author Nimrod M.
 */
public class ImageSelectorCommand implements ShellCommand {

    private static final String INVALID_ARGUMENT_MESSAGE = "Did not execute due to problem with image file.";
    private final AsciiArtAlgorithm asciiArtAlgorithm;

    /**
     * Constructs a new ImageSelectorCommand,
     * the image selected will be modified in the ASCII Art algorithm.
     * @param asciiArtAlgorithm The algorithm to be used for the ASCII Art
     */
    public ImageSelectorCommand(AsciiArtAlgorithm asciiArtAlgorithm) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
    }

    /**
     * @param arguments The arguments for the command - relative/absolute path to the image file
     * @throws ShellCommandException If the command was not executed successfully
     */
    @Override
    public void execute(String[] arguments) throws ShellCommandException {
        if (1 != arguments.length) {
            throw new ShellCommandException(INVALID_ARGUMENT_MESSAGE);
        }

        Image image;
        try {
            image = new SimpleImage(arguments[0]);
        } catch (IOException e) {
            throw new ShellCommandException(INVALID_ARGUMENT_MESSAGE);
        }

        asciiArtAlgorithm.setImage(image);
    }
}

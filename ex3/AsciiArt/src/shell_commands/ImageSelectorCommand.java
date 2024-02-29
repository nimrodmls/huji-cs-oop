package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import exceptions.ShellCommandException;
import image.Image;
import image.SimpleImage;

import java.io.IOException;

public class ImageSelectorCommand implements ShellCommand {

    private final String INVALID_ARGUMENT_MESSAGE = "Did not execute due to problem with image file.";
    private final AsciiArtAlgorithm asciiArtAlgorithm;

    public ImageSelectorCommand(AsciiArtAlgorithm asciiArtAlgorithm) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
    }

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

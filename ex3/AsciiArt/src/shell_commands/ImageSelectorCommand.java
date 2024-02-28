package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import exceptions.InvalidCommandFormatException;
import image.Image;
import image.SimpleImage;
import main.AsciiArt;

import java.io.IOException;

public class ImageSelectorCommand implements ShellCommand {

    private final String INVALID_ARGUMENT_MESSAGE = "Did not execute due to problem with image file.";
    private final AsciiArtAlgorithm asciiArtAlgorithm;

    public ImageSelectorCommand(AsciiArtAlgorithm asciiArtAlgorithm) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
    }

    @Override
    public void execute(String[] arguments) throws InvalidCommandFormatException {
        if (1 != arguments.length) {
            throw new InvalidCommandFormatException(INVALID_ARGUMENT_MESSAGE);
        }

        Image image;
        try {
            image = new SimpleImage(arguments[0]);
        } catch (IOException e) {
            throw new InvalidCommandFormatException(INVALID_ARGUMENT_MESSAGE);
        }

        asciiArtAlgorithm.setImage(image);
    }
}

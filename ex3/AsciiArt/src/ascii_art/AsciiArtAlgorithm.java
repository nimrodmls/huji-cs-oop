package ascii_art;

import image.BaseImage;
import image.PaddedImage;
import image.Image;
import image_char_matching.SubImgCharMatcher;

/**
 * This class is responsible for creating an ascii art representation of an image.
 * @author Nimrod M.
 */
public class AsciiArtAlgorithm {

    private final SubImgCharMatcher charMatcher;
    private PaddedImage destinationImage;
    private int resolution;
    private char[][] asciiArt = null;

    /**
     * Constructs a new AsciiArtAlgorithm object.
     * @param image The image to be converted to ascii art, the image will be padded to squared dimensions
     * @param resolution The number of sub images in each row
     * @param charset The characters to be used in the ascii art
     */
    public AsciiArtAlgorithm(BaseImage image, int resolution, char[] charset) {
        destinationImage = new PaddedImage(image);
        this.resolution = resolution;
        charMatcher = new SubImgCharMatcher(charset);
    }

    /**
     * Returns the charset used in the ascii art.
     * @return The charset used in the ascii art
     */
    public char[] getCharset() {
        return charMatcher.getCharset();
    }

    /**
     * Adds a character to the charset used in the ascii art.
     * @param c The character to be added to the charset
     */
    public void addChar(char c) {
        if (charMatcher.inCharset(c)) {
            return;
        }

        charMatcher.addChar(c);
        // Reset the asciiArt so that it will be recalculated - There are new characters in the charset
        asciiArt = null;
    }

    /**
     * Removes a character from the charset used in the ascii art.
     * @param c The character to be removed from the charset
     */
    public void removeChar(char c) {
        if (!charMatcher.inCharset(c)) {
            return;
        }

        charMatcher.removeChar(c);
        // Reset the asciiArt so that it will be recalculated - There are characters removed from the charset
        asciiArt = null;
    }

    /**
     * @return The resolution of the sub images
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * @param newResolution The new resolution of the sub images
     */
    public void setResolution(int newResolution) {
        if (newResolution == resolution) {
            return;
        }

        resolution = newResolution;
        // Reset the asciiArt so that it will be recalculated - There's a new subimage resolution
        asciiArt = null;
    }

    /**
     * @param newImage The new image to be converted to ascii art
     */
    public void setImage(BaseImage newImage) {
        destinationImage = new PaddedImage(newImage);
        // Reset the asciiArt so that it will be recalculated - There's a new image
        asciiArt = null;
    }

    /**
     * @return The image to be converted to ASCII art
     */
    public BaseImage getImage() {
        return destinationImage;
    }

    /**
     * Runs the algorithm and returns the ascii art representation of the image.
     * If the ASCII art was already calculated, the cached version will be returned.
     * @return The ASCII art representation of the image
     */
    public char[][] run() {
        // There's a cached version of the asciiArt
        if (null != asciiArt) {
            return asciiArt;
        }

        Image[][] subImages = destinationImage.getSubImages(resolution);
        char[][] asciiArt = new char[subImages.length][subImages[0].length];
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                Image currentSubImage = subImages[i][j];
                double brightness = currentSubImage.getImageBrightness();
                asciiArt[i][j] = charMatcher.getCharByImageBrightness(brightness);
            }
        }

        this.asciiArt = asciiArt;
        return asciiArt;
    }
}

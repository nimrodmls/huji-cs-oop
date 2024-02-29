package ascii_art;

import image.Image;
import image.PaddedImage;
import image.SimpleImage;
import image_char_matching.SubImgCharMatcher;

public class AsciiArtAlgorithm {

    private final SubImgCharMatcher charMatcher;
    private PaddedImage destinationImage;
    private int resolution;
    private char[][] asciiArt = null;

    public AsciiArtAlgorithm(Image image, int resolution, char[] charset) {
        destinationImage = new PaddedImage(image);
        this.resolution = resolution;
        charMatcher = new SubImgCharMatcher(charset);
    }

    public char[] getCharset() {
        return charMatcher.getCharset();
    }

    public void addChar(char c) {
        if (charMatcher.inCharset(c)) {
            return;
        }

        charMatcher.addChar(c);
        // Reset the asciiArt so that it will be recalculated - There are new characters in the charset
        asciiArt = null;
    }

    public void removeChar(char c) {
        if (!charMatcher.inCharset(c)) {
            return;
        }

        charMatcher.removeChar(c);
        // Reset the asciiArt so that it will be recalculated - There are characters removed from the charset
        asciiArt = null;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int newResolution) {
        if (newResolution == resolution) {
            return;
        }

        resolution = newResolution;
        // Reset the asciiArt so that it will be recalculated - There's a new subimage resolution
        asciiArt = null;
    }

    public void setImage(Image newImage) {
        destinationImage = new PaddedImage(newImage);
        // Reset the asciiArt so that it will be recalculated - There's a new image
        asciiArt = null;
    }

    public Image getImage() {
        return destinationImage;
    }

    public char[][] run() {
        // There's a cached version of the asciiArt
        if (null != asciiArt) {
            return asciiArt;
        }

        SimpleImage[][] subImages = destinationImage.getSubImages(resolution);
        char[][] asciiArt = new char[subImages.length][subImages[0].length];
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                SimpleImage currentSubImage = subImages[i][j];
                double brightness = currentSubImage.getImageBrightness();
                asciiArt[i][j] = charMatcher.getCharByImageBrightness(brightness);
            }
        }

        this.asciiArt = asciiArt;
        return asciiArt;
    }
}

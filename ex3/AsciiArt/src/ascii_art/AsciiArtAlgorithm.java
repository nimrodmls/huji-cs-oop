package ascii_art;

import image.Image;
import image.PaddedImage;
import image.SimpleImage;
import image_char_matching.SubImgCharMatcher;

public class AsciiArtAlgorithm {

    private final SubImgCharMatcher charMatcher;
    private PaddedImage destinationImage;
    private int resolution;

    public AsciiArtAlgorithm(Image image, int resolution, char[] charset) {
        destinationImage = new PaddedImage(image);
        this.resolution = resolution;
        charMatcher = new SubImgCharMatcher(charset);
    }

    public char[] getCharset() {
        return charMatcher.getCharset();
    }

    public void addChar(char c) {
        charMatcher.addChar(c);
    }

    public void removeChar(char c) {
        charMatcher.removeChar(c);
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int newResolution) {
        resolution = newResolution;
    }

    public void setImage(Image newImage) {
        destinationImage = new PaddedImage(newImage);
    }

    public char[][] run() {
        SimpleImage[][] subImages = destinationImage.getSubImages(resolution);
        char[][] asciiArt = new char[subImages.length][subImages[0].length];
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                SimpleImage currentSubImage = subImages[i][j];
                double brightness = currentSubImage.getImageBrightness();
                asciiArt[i][j] = charMatcher.getCharByImageBrightness(brightness);
            }
        }
        // TODO: Add caching of the result
        return asciiArt;
    }
}

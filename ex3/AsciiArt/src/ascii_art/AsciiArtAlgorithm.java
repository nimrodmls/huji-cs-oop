package ascii_art;

import image.Image;
import image.PaddedImage;
import image.SimpleImage;
import image_char_matching.SubImgCharMatcher;

public class AsciiArtAlgorithm {

    private final PaddedImage destinationImage;
    private final int resolution;
    private final SubImgCharMatcher charMatcher;

    public AsciiArtAlgorithm(Image image, int resolution, char[] charset) {
        destinationImage = new PaddedImage(image);
        this.resolution = resolution;
        charMatcher = new SubImgCharMatcher(charset);
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

        return asciiArt;
    }
}

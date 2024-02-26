package image;

import java.awt.*;
import java.io.IOException;

public class PaddedImage extends Image {

    private Image originalImage;
    private int paddedWidth;
    private int paddedHeight;

    public PaddedImage(Image image) {
        super(image.getPixelArray(), image.getWidth(), image.getHeight());
        originalImage = image;
        paddedWidth = roundToNextPowerOf2(image.getWidth());
        paddedHeight = roundToNextPowerOf2(image.getHeight());
    }

    @Override
    public int getWidth() {
        return paddedWidth;
    }

    @Override
    public int getHeight() {
        return paddedHeight;
    }

    /*public Image[] getSubImages(int subImageCount) {
        Image[] subImages = new Image[subImageCount];
        // The sub image is square, hence the width and height are the same.
        int subImageDimension = paddedWidth / subImageCount;

        // Creating each sub image
        for (int currentImageIdx = 0; currentImageIdx < subImageCount; currentImageIdx++) {


        }

        return subImages;
    }*/

    @Override
    public Color getPixel(int x, int y) {
        int xPadding = (paddedHeight - originalImage.getHeight()) / 2;
        int yPadding = (paddedWidth - originalImage.getWidth()) / 2;

        assert x > 0 && y > 0 && x < paddedWidth && y < paddedHeight : "The pixel is out of bounds";

        if (x < xPadding || // Checking the "left" padding
            y < yPadding || // Checking the "top" padding
            x >= originalImage.getHeight() + xPadding || // Checking the "right" padding
            y >= originalImage.getWidth() + yPadding) { // Checking the "bottom" padding
            return Color.WHITE;
        }
        // If the pixel is not in the padding area, we return the pixel from the original image
        return originalImage.getPixel(x-xPadding, y-yPadding);
    }

    /*private Image getSubImage(int x, int y, int dimension) {
        // The sub image is square, hence the width and height are the same.
        Color[][] subImagePixelArray = new Color[dimension][dimension];
        for (int row = 0; row < dimension; row++) {
            int currentImageRowOffset = y * row;
            for (int col = 0; col < dimension; col++) {

                int currentImageColOffset = x * col;
                if (currentImageRowOffset + row >= originalImage.getHeight() ||
                    currentImageColOffset + col >= originalImage.getWidth() ||
                    currentImageRowOffset
                ) {
                    subImagePixelArray[row][col] = Color.WHITE;
                }
                else {
                    subImagePixelArray[row][col] = originalImage.getPixel(currentImageRowOffset + row, currentImageColOffset + col);
                }
                subImagePixelArray[row][col] = originalImage.getPixel(currentImageIdx * subImageDimension + col, row);
            }
        }
        subImages[currentImageIdx] = new Image(subImagePixelArray, subImageDimension, subImageDimension);
    }*/

    private static int roundToNextPowerOf2(int n) {
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
    }
}

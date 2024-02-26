package image;

import java.awt.*;
import java.io.IOException;

public class PaddedImage extends Image {

    private final Image originalImage;
    private final int paddedWidth;
    private final int paddedHeight;
    private final int rowPadding;
    private final int colPadding;

    public PaddedImage(Image image) {
        super(image.getPixelArray(), image.getWidth(), image.getHeight());
        originalImage = image;
        paddedWidth = roundToNextPowerOf2(image.getWidth());
        paddedHeight = roundToNextPowerOf2(image.getHeight());
        rowPadding = (paddedHeight - originalImage.getHeight()) / 2;
        colPadding = (paddedWidth - originalImage.getWidth()) / 2;
    }

    @Override
    public int getWidth() {
        return paddedWidth;
    }

    @Override
    public int getHeight() {
        return paddedHeight;
    }

    public Image[][] getSubImages(int subImageCountInRow) {
        // The sub image is square, hence the width and height are the same.
        int subImageDimension = paddedWidth / subImageCountInRow;
        int subImageRows = paddedHeight / subImageDimension;
        Image[][] subImages = new Image[subImageRows][subImageCountInRow];

        for (int currentSubImageRow = 0; currentSubImageRow < subImageRows; currentSubImageRow++) {
            for (int currentSubImageCol = 0; currentSubImageCol < subImageCountInRow; currentSubImageCol++) {
                subImages[currentSubImageRow][currentSubImageCol] =
                        getSubImage(
                                currentSubImageRow * subImageDimension,
                                currentSubImageCol * subImageDimension,
                                subImageDimension);
            }
        }

        return subImages;
    }

    private Image getSubImage(int x, int y, int dimension) {
        Color[][] subImagePixelArray = new Color[dimension][dimension];
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                subImagePixelArray[row][col] = getPixel(x + row, y + col);
            }
        }
        return new Image(subImagePixelArray, dimension, dimension);
    }

    @Override
    public Color getPixel(int x, int y) {
        // The padding is virtual and not part of the original image!

        assert x > 0 && y > 0 && x < paddedWidth && y < paddedHeight : "The pixel is out of bounds";

        if (x < rowPadding || // Checking the "left" padding
            y < colPadding || // Checking the "top" padding
            x >= originalImage.getHeight() + rowPadding || // Checking the "right" padding
            y >= originalImage.getWidth() + colPadding) { // Checking the "bottom" padding
            return Color.WHITE;
        }
        // If the pixel is not in the padding area, we return the pixel from the original image
        return originalImage.getPixel(x-rowPadding, y-colPadding);
    }

    private static int roundToNextPowerOf2(int n) {
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
    }
}

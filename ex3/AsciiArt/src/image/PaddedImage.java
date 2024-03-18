package image;

import java.awt.*;

/**
 * Representing an image padded to nearest square.
 * @author Nimrod M.
 */
public class PaddedImage extends BaseImage {

    private final BaseImage originalImage;
    private final int paddedWidth;
    private final int paddedHeight;
    private final int rowPadding;
    private final int colPadding;

    /**
     * Constructs a new PaddedImage object using the given image.
     * If the image is not squared, it will be padded to the nearest square,
     * otherwise it will remain the same.
     * @param image The image to be padded
     */
    public PaddedImage(BaseImage image) {
        originalImage = image;
        paddedWidth = roundToNextPowerOf2(image.getWidth());
        paddedHeight = roundToNextPowerOf2(image.getHeight());
        // The padding added to the original image,
        // these are balanced on both sides of the image respectively
        rowPadding = (paddedHeight - originalImage.getHeight()) / 2;
        colPadding = (paddedWidth - originalImage.getWidth()) / 2;
    }

    /**
     * @return The width of the padded image
     */
    @Override
    public int getWidth() {
        return paddedWidth;
    }

    /**
     * @return The height of the padded image
     */
    @Override
    public int getHeight() {
        return paddedHeight;
    }

    /**
     * Retrieves the pixel color values at the given coordinates.
     * If the pixel is in the padding area, it will return white.
     * @param x The row coordinate of the pixel
     * @param y The column coordinate of the pixel
     * @return The pixel color values at the given coordinates
     */
    @Override
    public Color getPixel(int x, int y) {
        assert x > 0 && y > 0 && x < paddedWidth && y < paddedHeight : "The pixel is out of bounds";

        // The padding is virtual and not part of the original image!
        if (x < rowPadding || // Checking the "top" padding
            y < colPadding || // Checking the "left" padding
            x >= originalImage.getHeight() + rowPadding || // Checking the "bottom" padding
            y >= originalImage.getWidth() + colPadding) {  // Checking the "right" padding
            return Color.WHITE;
        }
        // If the pixel is not in the padding area, we return the pixel from the original image
        return originalImage.getPixel(x-rowPadding, y-colPadding);
    }

    /**
     * @param resolution The number of sub images in each row
     * @return A 2D array of sub images
     */
    public Image[][] getSubImages(int resolution) {
        // The sub image is square, hence the width and height are the same.
        int subImageDimension = paddedWidth / resolution;
        int subImageRows = paddedHeight / subImageDimension;
        Image[][] subImages = new Image[subImageRows][resolution];

        for (int currentSubImageRow = 0; currentSubImageRow < subImageRows; currentSubImageRow++) {
            for (int currentSubImageCol = 0; currentSubImageCol < resolution; currentSubImageCol++) {
                subImages[currentSubImageRow][currentSubImageCol] =
                        getSubImage(
                                currentSubImageRow * subImageDimension,
                                currentSubImageCol * subImageDimension,
                                subImageDimension);
            }
        }

        return subImages;
    }

    /**
     * Creates a single sub image from the padded image,
     * at the given coordinates in respect to the given dimensions.
     * @param x The row coordinate of the sub image
     * @param y The column coordinate of the sub image
     * @param dimension The dimension of the sub image
     * @return A sub image of the padded image
     */
    private Image getSubImage(int x, int y, int dimension) {
        Color[][] subImagePixelArray = new Color[dimension][dimension];
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                subImagePixelArray[row][col] = getPixel(x + row, y + col);
            }
        }
        return new Image(subImagePixelArray, dimension, dimension);
    }

    /**
     * Rounds the given number to the next power of 2 that is greater or equal to n.
     * @param n The number to be rounded
     * @return The next power of 2 that is greater or equal to n
     * @see https://stackoverflow.com/questions/466204/rounding-up-to-next-power-of-2
     */
    private static int roundToNextPowerOf2(int n) {
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
    }
}

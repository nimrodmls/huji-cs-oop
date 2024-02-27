package image;

import java.awt.*;

public abstract class Image {
    private static final double RED_GRAYSCALE_WEIGHT = 0.2126;
    private static final double GREEN_GRAYSCALE_WEIGHT = 0.7152;
    private static final double BLUE_GRAYSCALE_WEIGHT = 0.0722;

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract Color getPixel(int x, int y);

    public Color[][] getPixelArray() {
        Color[][] pixelArray = new Color[getHeight()][getWidth()];
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                pixelArray[i][j] = getPixel(i, j);
            }
        }
        return pixelArray;
    }

    public double getImageBrightness() {
        Color[][] pixelArray = getPixelArray();
        double grayscaleSum = 0;
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                Color currentPixel = pixelArray[row][col];
                double red = currentPixel.getRed() * RED_GRAYSCALE_WEIGHT;
                double green = currentPixel.getGreen() * GREEN_GRAYSCALE_WEIGHT;
                double blue = currentPixel.getBlue() * BLUE_GRAYSCALE_WEIGHT;
                grayscaleSum += red + green + blue;
            }
        }
        // Normalize the sum to be between 0 and 1
        return grayscaleSum / (getHeight() * getWidth() * 255);
    }
}

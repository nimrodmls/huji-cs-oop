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

    public static double[][] getGrayscalePixels(SimpleImage image) {
        Color[][] pixelArray = image.getPixelArray();
        double[][] grayscale = new double[image.getHeight()][image.getWidth()];
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                Color currentPixel = pixelArray[row][col];
                double red = currentPixel.getRed() * RED_GRAYSCALE_WEIGHT;
                double green = currentPixel.getGreen() * GREEN_GRAYSCALE_WEIGHT;
                double blue = currentPixel.getBlue() * BLUE_GRAYSCALE_WEIGHT;
                grayscale[row][col] = red + green + blue;
            }
        }
        return grayscale;
    }
}

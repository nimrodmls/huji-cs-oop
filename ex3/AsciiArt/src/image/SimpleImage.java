package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A simple (raw) representation of an image.
 * @author Nimrod M.
 */
public class SimpleImage extends Image {

    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * Constructs a new SimpleImage object using the given file.
     * @param filename The name of the file to be read the image from
     * @throws IOException If the file is not found
     */
    public SimpleImage(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();


        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }
    }

    /**
     * Constructs a new SimpleImage object using the given pixel array (and its dimensions).
     * @param pixelArray The pixel array of the image
     * @param width The width of the image
     * @param height The height of the image
     */
    public SimpleImage(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    /**
     * @return The width of the image
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the image
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the pixel color values at the given coordinates.
     * @param x The row coordinate of the pixel
     * @param y The column coordinate of the pixel
     * @return The pixel color values at the given coordinates
     */
    @Override
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * Saves the image to a file.
     * @param fileName The name of the file to save the image to
     */
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

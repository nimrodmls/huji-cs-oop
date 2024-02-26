package danogl.gui.rendering;

import danogl.util.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A simple renderable - a still image. This class is immutable.
 * @author Dan Nirel
 */
public class ImageRenderable implements Renderable {
    public static final ImageRenderable DEFAULT_IMAGE = createDefaultImage();

    private final BufferedImage image;
    private final ConfigureGraphics configureGraphics = new ConfigureGraphics();

    /**
     * Constructor. Typically, an instance of this class will be created via
     * {@link danogl.gui.ImageReader}.
     */
    public ImageRenderable(BufferedImage image) { this.image = image; }

    @Override
    public void render(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions,
                       double degreesCounterClockwise,
                       boolean isFlippedHorizontally, boolean isFlippedVertically,
                       double opaqueness) {
        if(image == null || opaqueness <= 0)
            return;

        int topLeftCornerX = (int)topLeftCorner.x();
        int topLeftCornerY = (int)topLeftCorner.y();
        int dimX = (int)dimensions.x();
        int dimY = (int)dimensions.y();
        if(isFlippedHorizontally) {
            dimX *= -1;
            topLeftCornerX -= dimX;
            degreesCounterClockwise *= -1;
        }
        if(isFlippedVertically) {
            dimY *= -1;
            topLeftCornerY -= dimY;
            degreesCounterClockwise *= -1;
        }

        configureGraphics.init(g, topLeftCorner, dimensions, degreesCounterClockwise, opaqueness);
        configureGraphics.setRotation();
        configureGraphics.setOpaqueness();

        g.drawImage(image,
                topLeftCornerX, topLeftCornerY,
                dimX, dimY, null);

        configureGraphics.rollbackChanges();
    }

    /**
     * Returns the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * The image's original width, in pixels (unrelated to how large it's rendered in the game)
     */
    public int width() { return image.getWidth(null); }
    /**
     * The image's original height, in pixels (unrelated to how large it's rendered in the game)
     */
    public int height() { return image.getHeight(null); }

    /**
     * The image's original ratio (width/height). Unrelated to it's dimensions in the game)
     */
    public float ratioAsWidthDivHeight() {
        return (float)image.getWidth(null)
                /image.getHeight(null);
    }

    /**
     * Returns a cropped version of the image.
     * @param topLeft pixel coordinates of top-left rectangle to crop from the image.
     *                    The pixel at these coordinates is included. If cropping is not desired, set to Vector2.ZERO.
     * @param bottomRight pixel coordinates of bottom-right rectangle to crop from the image.
     *                        The pixel at these coordinates is excluded. If cropping is not desired, set to Vector2.ZERO.
     * @return a new, smaller, ImageRenderable. The original remains unchanged. If the input
     * is invalid, returns null.
     */
    public ImageRenderable crop(Vector2 topLeft, Vector2 bottomRight) {
        if (topLeft.x() >= bottomRight.x() || topLeft.y() >= bottomRight.y() ||
                topLeft.x() < 0 || topLeft.y() < 0 || bottomRight.x() > image.getWidth() ||
                bottomRight.y() > image.getHeight()) {
            return null;
        }

        int width = (int)bottomRight.x()-(int)topLeft.x();
        int height = (int)bottomRight.y()-(int)topLeft.y();
        BufferedImage croppedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for(int y = 0 ; y < height ; y++) {
            for (int x = 0; x < width ; x++) {
                int thisPixel = image.getRGB((int)topLeft.x()+x, (int)topLeft.y()+y);
                croppedImage.setRGB(x, y, thisPixel);
            }
        }
        return new ImageRenderable(croppedImage);
    }

    private static ImageRenderable createDefaultImage() {
        final int DIM = 10;
        final Color color = Color.MAGENTA;
        var bufferedImage = new BufferedImage(DIM, DIM, BufferedImage.TYPE_INT_ARGB);
        for(int y = 0 ; y < DIM ; y++) {
            for (int x = 0; x < DIM ; x++) {
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }
        return new ImageRenderable(bufferedImage);
    }
}

package danogl.gui.rendering;

import danogl.gui.ImageReader;
import danogl.gui.MsgLevel;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;

/**
 * A dynamic, repeating animation.
 * @author Dan Nirel
 */
public class AnimationRenderable implements Renderable {
    private Renderable[] clips;
    private double timeBetweenClips;
    private int currImIndex = 0;
    private double timeSinceLastClipSwitch = 0;
    private boolean repeat = true;

    /**
     * Creates an animation from a spritesheet - a single image file that contains multiple
     * images successively. The order is assumed to be left to right.
     * This method does not support reading only part of the row, for that you'd need to crop
     * it first (either externally or with RenderableImage.crop).
     * Also supports reading a single row from a spritesheet with multiple animations.
     * @param spritesheetPath Absolute or relative path to the spritesheet image file.
     * @param rowCount If the spritesheet contains only one animation, this would be 1.
     *                 Otherwise, specify how many animations are contained in the spritesheet.
     * @param colCount How many sprites in the animation (number of columns). This should be at least 1.
     * @param rowIndex If the spritesheet contains only one row of animation, this would be 0.
     *                 If multiple rows exist, a non-negative value means using the row with this index.
     *                 A negative value means using all rows as a single animation (left to right, top to bottom)
     * @param imageReader To read the images with.
     * @param useTransparency Should the top-left pixel of the spritesheet be considered
     *                        a transparent color for all images.
     * @param timeBetweenClips The time to wait before moving to the next clip.
     */
    public AnimationRenderable(
            String spritesheetPath,
            int rowCount,
            int colCount,
            int rowIndex,
            ImageReader imageReader,
            boolean useTransparency, double timeBetweenClips) {

        if(colCount < 1)
            throw new InvalidParameterException("The number of sprites in the animation should be at least 1");

        var spriteSheet = imageReader.readImage(spritesheetPath, useTransparency);
        //if rowIndex<0, the whole spritesheet is a single animation
        int spriteCount = rowIndex >= 0 ? colCount : rowCount * colCount;

        Renderable[] sprites = new Renderable[spriteCount];
        int spriteHeight = spriteSheet.height()/rowCount;
        int spriteWidth = spriteSheet.width()/colCount;

        if(rowIndex < 0) //if using the whole spritesheet, start from row 0
            rowIndex = 0;
        MutableVector2 topLeft = new MutableVector2(0, spriteHeight * rowIndex);
        MutableVector2 bottomRight = new MutableVector2(spriteWidth, spriteHeight*(rowIndex+1));
        Vector2 delta = Vector2.of(spriteWidth, 0);

        for (int i = 0; i < spriteCount; i++) {
            sprites[i] = spriteSheet.crop(topLeft,bottomRight);
            topLeft.selfAdd(delta);
            if(topLeft.x()+spriteWidth > spriteSheet.width())
                topLeft.setXY(0, topLeft.y()+spriteHeight);
            bottomRight.setXY(topLeft.x() + spriteWidth, topLeft.y() + spriteHeight);
        }

        this.clips = sprites;
        this.timeBetweenClips = timeBetweenClips;
    }

    /**
     * Initialize the animation using Renderables
     * @param clips The animation's clips.
     * @param timeBetweenClips The time to wait before moving to the next clip.
     */
    public AnimationRenderable(Renderable[] clips, double timeBetweenClips) {
        this.clips = clips.clone();
        this.timeBetweenClips = timeBetweenClips;
    }

    /**
     * Initialize the animation using file-paths of images on disk
     * @param imagePaths Paths of images on disk.
     * @param imageReader To read the images with.
     * @param useTransparency Should the top-left pixel of every image be considered
     *                        a transparent color for that image.
     * @param timeBetweenClips The time to wait before moving to the next clip.
     */
    public AnimationRenderable(String[] imagePaths, ImageReader imageReader,
                               boolean useTransparency, double timeBetweenClips) {
        populateClipsWithImages(imagePaths, imageReader, useTransparency, timeBetweenClips);
    }

    /**
     * Initialize the animation using a directory containing images only. The images
     * are sorted by name to determing the animation's order.
     * @param directoryOfImages A directory path; the directory should contain image files only.
     * @param imageReader To read the images with.
     * @param useTransparency Should the top-left pixel of every image be considered
     *                        a transparent color for that image.
     * @param timeBetweenClips The time to wait before moving to the next clip.
     */
    public AnimationRenderable(String directoryOfImages, ImageReader imageReader,
                               boolean useTransparency, double timeBetweenClips) {
        String[] imagePaths = null;
        try {
            imagePaths = Files.list(new File(directoryOfImages).toPath())
                    .map(Path::toString)
                    .sorted()
                    .toArray(String[]::new);
            if(imagePaths.length == 0)
                throw new IOException("directory is empty");
        }
        catch(IOException ioe) {
            imageReader.messageHandler().showMessage(
                    String.format("Failed to read images from directory '%s': %s",
                            directoryOfImages, ioe.getMessage()),
                    MsgLevel.WARNING);
            clips = new ImageRenderable[] { ImageRenderable.DEFAULT_IMAGE };
            this.timeBetweenClips = timeBetweenClips;
            return;
        }
        populateClipsWithImages(imagePaths, imageReader, useTransparency, timeBetweenClips);
    }

    @Override
    public void update(double deltaTime) {
        timeSinceLastClipSwitch += deltaTime;
        if(timeSinceLastClipSwitch >= timeBetweenClips) {
            timeSinceLastClipSwitch = 0;
            currImIndex++;
            if(currImIndex >= clips.length && repeat)
                currImIndex = 0;
        }
    }

    @Override
    public void render(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions,
                       double degreesCounterClockwise,
                       boolean isFlippedHorizontally, boolean isFlippedVertically,
                       double opaqueness) {
        if(currImIndex >= clips.length)
            return; //then animation ended, render nothing
        clips[currImIndex].render(
                g, topLeftCorner, dimensions, degreesCounterClockwise,
                isFlippedHorizontally, isFlippedVertically, opaqueness);
    }

    /**Reset the animation (go back to the first frame).*/
    public void resetAnimation() {
        currImIndex = 0;
        timeSinceLastClipSwitch = 0;
    }

    public boolean isRepeating() { return repeat; }
    public void setRepeat(boolean shouldRepeat) { repeat = shouldRepeat; }

    private void populateClipsWithImages(
            String[] imagePaths,
            ImageReader imageReader,
            boolean useTransparency,
            double timeBetweenClips) {
        this.timeBetweenClips = timeBetweenClips;
        clips = new ImageRenderable[imagePaths.length];
        for (int i = 0; i < clips.length; i++) {
            clips[i] = imageReader.readImage(imagePaths[i], useTransparency);
        }
    }
}

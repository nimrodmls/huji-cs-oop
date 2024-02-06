package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class UserInterface extends GameObject {
    private final float HEART_WIDTH = 20;
    private final float HEART_SPACING = 10;

    private final Vector2 dimensions;
    private final BrickerGameManager gameManager;
    private final TextRenderable heartCounter;
    private GameObject[] hearts;
    private int heartCount = 0;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param gameManager   The game manager using the UI.
     */
    public UserInterface(Vector2 topLeftCorner,
                         Vector2 dimensions,
                         BrickerGameManager gameManager,
                         int maxHeartCount) {
        super(topLeftCorner, dimensions, null);
        this.dimensions = dimensions;
        this.gameManager = gameManager;
        hearts = new GameObject[maxHeartCount];
        heartCounter = new TextRenderable(Integer.toString(heartCount));
        heartCounter.setColor(Color.green);

        GameObject heartCounterObject = new GameObject(
                new Vector2(
                        getTopLeftCorner().x() - 50,
                        getTopLeftCorner().y() - 50),
                new Vector2(30, 30),
                heartCounter);
        gameManager.addGameObject(heartCounterObject, Layer.UI);
    }

    public void addHeart(ImageReader imageReader) {
        if (heartCount >= hearts.length) {
            return; // Reached capacity of hearts
        }

        Renderable heartImage = imageReader.readImage(
                "asserts/heart.png", true);
        float heart_width = BrickerGameManager.calculateObjectWidthInRow(
                hearts.length, HEART_SPACING, dimensions.x());
        hearts[heartCount] = new GameObject(
                new Vector2(
                        getTopLeftCorner().x() + (heartCount * (heart_width + HEART_SPACING)),
                        getTopLeftCorner().y()),
                new Vector2(heart_width, heart_width),
                heartImage);
        gameManager.addGameObject(hearts[heartCount], Layer.UI);
        heartCount++;

        updateHeartCounter();
    }

    public void removeHeart() {
        if (heartCount <= 0) {
            return; // No hearts to remove
        }

        gameManager.removeGameObject(hearts[heartCount -1], Layer.UI);
        // Removing the heart from the UI's records
        hearts[heartCount -1] = null;
        heartCount--;

        updateHeartCounter();
    }

    private void updateHeartCounter() {
        switch (heartCount) {
            case 1:
                heartCounter.setColor(Color.RED);
                break;
            case 2:
                heartCounter.setColor(Color.YELLOW);
                break;
            default:
                heartCounter.setColor(Color.GREEN);
                break;
        }
        heartCounter.setString(Integer.toString(heartCount));
    }
}

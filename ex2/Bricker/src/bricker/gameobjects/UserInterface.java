package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class UserInterface extends GameObject {
    private final float HEART_WIDTH = 20;
    private final float HEART_SPACING = 10;
    private final BrickerGameManager gameManager;
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
        this.gameManager = gameManager;
        hearts = new GameObject[maxHeartCount];
    }

    public void addHeart(ImageReader imageReader) {
        if (heartCount >= hearts.length) {
            return; // Reached capacity of hearts
        }

        Renderable heartImage = imageReader.readImage(
                "asserts/heart.png", true);
        hearts[heartCount] = new GameObject(
                new Vector2(
                        getTopLeftCorner().x() + (heartCount * HEART_SPACING),
                        getTopLeftCorner().y()),
                new Vector2(HEART_WIDTH, HEART_WIDTH),
                heartImage);
        gameManager.addGameObject(hearts[heartCount], Layer.UI);
        heartCount++;
    }

    public void removeHeart() {
        if (heartCount <= 0) {
            return; // No hearts to remove
        }

        gameManager.removeGameObject(hearts[heartCount -1], Layer.UI);
        // Removing the heart from the UI's records
        hearts[heartCount -1] = null;
        heartCount--;
    }

}

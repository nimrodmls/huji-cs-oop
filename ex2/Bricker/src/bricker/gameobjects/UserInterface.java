package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import bricker.utilities.GameConstants;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class UserInterface extends GridGameObject {
    private static final float ELEMENT_SPACING = 10.0f;

    private final BrickerGameManager gameManager;
    private TextRenderable heartCounter;
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
                         Vector2 elementDimensions,
                         BrickerGameManager gameManager,
                         int maxHeartCount) {
        super(topLeftCorner, dimensions, elementDimensions, ELEMENT_SPACING, gameManager);
        this.gameManager = gameManager;
        hearts = new GameObject[maxHeartCount];
    }

    public void addHeart(ImageReader imageReader) {

        if (heartCount >= hearts.length) {
            return; // Reached capacity of hearts, ignore
        }

        Renderable heartImage = imageReader.readImage(
                GameConstants.HEART_ASSET_PATH, true);

        hearts[heartCount] = new GameObject(getTopLeftCorner(), getDimensions(), heartImage);
        super.addObject(heartCount, 1, hearts[heartCount], Layer.UI);
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
        if (null == heartCounter) {

            heartCounter = new TextRenderable(Integer.toString(heartCount));
            heartCounter.setColor(Color.green);

            GameObject heartCounterObject = new GameObject(getTopLeftCorner(), getDimensions(), heartCounter);
            super.addObject(0, 0, heartCounterObject, Layer.UI);
        }

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

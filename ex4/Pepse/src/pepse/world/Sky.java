package pepse.world;

import java.awt.*;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

/**
 * The sky in the game. The sky is a blue rectangle that covers the entire window,
 * and moves with the camera. It shouldn't be interacted with.
 */
public class Sky {

    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private static final String SKY_TAG = "sky";

    /**
     * Create a new sky game object
     * @param windowDimensions The dimensions of the window
     * @return A new sky game object
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        // Setting the sky to move with the camera
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }
}

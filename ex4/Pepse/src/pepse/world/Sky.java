package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sky {

    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    public static final String skyTag = "sky";

    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        // Setting the sky to move with the camera
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(skyTag);
        return sky;
    }
}

package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;

import java.awt.*;

/**
 * The sun's halo in the game. The sun's halo is a yellow circle that surrounds the sun.
 * @author Nimrod M.
 */
public class SunHalo {
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static final String SUN_HALO_TAG = "sunHalo";
    private static final float HALO_SIZE_FACTOR = 1.5f;

    /**
     * Create a new sun halo game object, and associates it with the sun.
     * @param sun The sun game object
     * @return A new sun halo game object
     */
    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(
                sun.getTopLeftCorner(),
                // The halo's size is 1.5 times the sun's size
                sun.getDimensions().mult(HALO_SIZE_FACTOR),
                new OvalRenderable(HALO_COLOR));
        // Setting the sun to move with the camera
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        // Setting the halo to be with the sun
        sunHalo.setCenter(sun.getCenter());

        // Allowing the halo to move with the sun
        sunHalo.addComponent((deltaTime -> {
            sunHalo.setCenter(sun.getCenter());
        }));

        return sunHalo;
    }
}

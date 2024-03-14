package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static final String sunHaloTag = "sunHalo";

    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(
                sun.getTopLeftCorner(),
                // The halo's size is 1.5 times the sun's size
                sun.getDimensions().mult(1.5f),
                new OvalRenderable(HALO_COLOR));
        // Setting the sun to move with the camera
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(sunHaloTag);
        // Setting the halo to be with the sun
        sunHalo.setCenter(sun.getCenter());



        return sunHalo;
    }
}

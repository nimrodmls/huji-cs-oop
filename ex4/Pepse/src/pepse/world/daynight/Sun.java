package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

import pepse.util.GameConstants;

public class Sun {
    private static final Color SUN_COLOR = Color.YELLOW;
    private static final Vector2 SUN_SIZE = new Vector2(50, 50);
    public static final String sunTag = "sun";

    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(
                Vector2.ZERO,
                SUN_SIZE,
                new OvalRenderable(SUN_COLOR));
        // Setting the sun to move with the camera
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(sunTag);

        float yCycleCenter = GameConstants.INITIAL_GROUND_HEIGHT_FACTOR * windowDimensions.y();
        Vector2 cycleCenter = new Vector2(
                windowDimensions.x() / 2,
                yCycleCenter);
        // Setting the initial sun center to be exactly above the cycle center
        Vector2 initialSunCenter = new Vector2(
                windowDimensions.x() / 2,
                yCycleCenter - (windowDimensions.y() / 3));
        sun.setCenter(initialSunCenter);
        new Transition<Float>(
                sun,
                (Float angle) -> sun.setCenter(
                        initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)
                ),
                0.0f,
                360.0f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }
}

package pepse.world.daynight;

import java.awt.*;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import pepse.util.GameConstants;

/**
 * The sun in the game. The sun moves in a circular path, and its position is updated every frame.
 * @author Nimrod M.
 */
public class Sun {
    private static final float SUN_START_ANGLE = 0.0f;
    private static final float SUN_END_ANGLE = 360.0f;
    private static final Color SUN_COLOR = Color.YELLOW;
    private static final Vector2 SUN_SIZE = new Vector2(50, 50);
    private static final String SUN_TAG = "sun";

    /**
     * Create a new sun game object
     * @param windowDimensions The dimensions of the window
     * @param cycleLength The length of the day-night cycle, in seconds
     * @return A new sun game object
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(
                Vector2.ZERO,
                SUN_SIZE,
                new OvalRenderable(SUN_COLOR));
        // Setting the sun to move with the camera
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

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
                SUN_START_ANGLE,
                SUN_END_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }
}

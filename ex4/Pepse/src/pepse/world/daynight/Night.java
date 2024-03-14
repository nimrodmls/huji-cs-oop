package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {
    private static final float MIDNIGHT_OPACITY = 0.5f;
    private static final Color NIGHT_COLOR = Color.BLACK;
    private static final String nightTag = "night";

    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject nightSky = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(NIGHT_COLOR));
        // Setting the night sky to move with the camera
        nightSky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        nightSky.setTag(nightTag);

        // Creating the night transition cycle
        new Transition<Float>(
                nightSky,
                nightSky.renderer()::setOpaqueness,
                0.0f,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                // A day consists of Midnight->Midday->Midnight
                cycleLength / 2.0f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return nightSky;
    }
}

package danogl.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Utility class for creating border objects around the window with which
 * GameObjects would collide.
 * <br>Note: the borders, on their side, are set to collide. For them to stop
 * a game object, it needs to be configured to collide as well using
 * {@link GameObjectPhysics#preventIntersectionsFromDirection(Vector2)}, e.g.
 * gameObject.physics().preventIntersectionsFromDirection(Vector2.ZERO) .
 * @author Dan Nirel
 */
public class Border {
    /**
     * Create a border. This border is not added to the GameObjectCollection and should be added
     * by the user, probably to the layer Layer.STATIC_OBJECTS.
     * <br>Note: the created border is in the default coordinate space - world space. You can change
     * this using the returned object's setCoordinateSpace method.
     * @param dir one of the four cordinal directions: Vector2.UP, Vector2.DOWN, Vector2.LEFT, or Vector2.RIGHT
     * @param windowDimensions as returned by windowController.windowDimensions()
     * @param frameWidth positive values will cause the border to enter the frame by this amount.
     *                   Negative values will cause the border to be outside the window's frame
     *                   by this amount.
     * @param color the border's color if desired, null if the border should be invisible
     * @return a GameObject representing the border. This object should be added to
     * the game's GameObjectCollection at the desired layer, probably Layer.STATIC_OBJECTS.
     */
    public static GameObject atDirection(Vector2 dir, Vector2 windowDimensions, float frameWidth, Color color) {
        MutableVector2 topLeft = new MutableVector2(Vector2.ZERO);
        if(dir.equals(Vector2.UP))
            topLeft.setXY(0, -windowDimensions.y());
        else if(dir.equals(Vector2.DOWN))
            topLeft.setXY(0, windowDimensions.y());
        else if(dir.equals(Vector2.LEFT))
            topLeft.setXY(-windowDimensions.x(), 0);
        else if(dir.equals(Vector2.RIGHT))
            topLeft.setXY(windowDimensions.x(), 0);
        else
            throw new IllegalArgumentException("dir parameter in Border.atDir should be one of the four cardinal directions");

        Renderable renderable = color == null ? null : new RectangleRenderable(color);
        Vector2 dimensions = windowDimensions;

        if(frameWidth < 0) {
            dimensions = dimensions.add(
                    Vector2.of(Math.abs(dir.y()), Math.abs(dir.x())).mult(-frameWidth * 2));
            topLeft.selfAdd(windowDimensions.mult(.5f)).selfSubtract(dimensions.mult(.5f));
        }

        GameObject border = new GameObject(
                topLeft.subtract(dir.mult(frameWidth)), dimensions, renderable);
        border.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        border.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        return border;
    }

    /**
     * Add all four borders to the GameObjectCollection in the specified layer
     * @param windowDimensions as returned by windowController.windowDimensions()
     * @param frameWidth positive values will cause the border to enter the frame by this amount.
     *                   Negative values will cause the border to be outside the window's frame
     *                   by this amount.
     * @param color the border's color if desired, null if the border should be invisible
     * @param gameObjects the game's GameObjectCollection, to which the borders should be added
     * @param layer the layer in which to add the borders
     * @return an array of 4 GameObjects, corresponding to the borders in the directions
     * up, down, left, and right (in this order)
     */
    public static GameObject[] addCompleteFrame(Vector2 windowDimensions, float frameWidth, Color color,
                                GameObjectCollection gameObjects, int layer) {

        GameObject[] borders = Arrays.stream(
                new Vector2[] {Vector2.UP, Vector2.DOWN, Vector2.LEFT, Vector2.RIGHT})
                .map(dir->atDirection(dir, windowDimensions, frameWidth, color))
                .toArray(GameObject[]::new);

        //add to gameObjects
        Arrays.stream(borders).forEach(border->gameObjects.addGameObject(border, layer));

        return borders;
    }
}

package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import bricker.main.BrickerGameManager;
import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BrickGrid extends GridGameObject {

    // Defining the spacing between bricks
    private static final float BRICK_SPACING = 3.0f;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        X = Number of Rows, Y = Number of Columns.
     * @param brickHeight       Height of each brick in pixels.
     * @param gameManager       The game manager that will manage this object.
     */
    public BrickGrid(Vector2 topLeftCorner, Vector2 dimensions, float brickHeight, float totalLength, BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, new Vector2(calculateObjectWidth(dimensions.x(), totalLength), brickHeight), BRICK_SPACING, gameManager);
    }

    public void addObject(int coordX, int coordY, Renderable brickRenderable, CollisionStrategy collisionStrategy) {
        Brick brick = new Brick(
                getTopLeftCorner(),
                getDimensions(),
                brickRenderable,
                collisionStrategy);
        super.addObject(coordX, coordY, brick, Layer.FOREGROUND);
    }

    private static float calculateObjectWidth(float objectCount,
                                              float rowLength) {
        return (rowLength - ((objectCount - 1) * BRICK_SPACING)) / objectCount;
    }
}

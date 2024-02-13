package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import bricker.brick_strategies.CollisionStrategy;
import bricker.main.BrickerGameManager;

/**
 * Represents a grid of bricks on the game board.
 * @author Nimrod M.
 */
public class BrickGrid extends GridGameObject {

    private static final int BRICKS_LAYER = Layer.FOREGROUND;
    // Spacing between each brick on the grid
    private static final float BRICK_SPACING = 3.0f;

    private final BrickerGameManager gameManager;
    private int brickCount = 0;

    /**
     * Construct a new Brick Grid.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        X = Number of Rows, Y = Number of Columns.
     * @param brickHeight       Height of each brick in pixels.
     * @param gameManager       The game manager that will manage this object.
     */
    public BrickGrid(Vector2 topLeftCorner,
                     Vector2 dimensions,
                     float brickHeight,
                     float totalLength,
                     BrickerGameManager gameManager) {
        // The width of each brick is calculated according to the total length of the row
        super(topLeftCorner, dimensions,
              new Vector2(calculateObjectWidth(dimensions.x(), totalLength), brickHeight),
              BRICK_SPACING, gameManager);
        this.gameManager = gameManager;
    }

    /**
     * Adds a new brick to the grid on the given coordinates.
     * Assumes validity of coordinates and that they are not occupied.
     *
     * @param coordX            X coordinate of the grid cell to place the brick in.
     * @param coordY            Y coordinate of the grid cell to place the brick in.
     * @param brickRenderable   The renderable representing the brick. Can be null, in which case
     * @param collisionStrategy the strategy applied to the brick.
     */
    public void addObject(int coordX,
                          int coordY,
                          Renderable brickRenderable,
                          CollisionStrategy collisionStrategy) {
        // Note that the dimensions given to the brick here are mostly irrelevant,
        // as the brick will be resized according to the grid's dimensions.
        Brick brick = new Brick(
                getTopLeftCorner(),
                getDimensions(),
                brickRenderable,
                collisionStrategy);
        super.addObject(coordX, coordY, brick, BRICKS_LAYER);
        brickCount++;
    }

    /**
     * Removes a GameObject from the grid by reference.
     * (this class doesn't offer removing by grid coordinates, as it is not a needed functionality)
     * 
     * @param object The GameObject to remove from the grid.
     */
    public void removeObject(GameObject object) {
        if (0 == brickCount) {
            return;
        }

        // Only if the removal is successful, we will decrease the brick count
        if (gameManager.removeGameObject(object, BRICKS_LAYER)) {
            brickCount--;
        }
    }

    /**
     * @return The number of bricks in the grid.
     */
    public int getBrickCount() {
        return brickCount;
    }

    /**
     * Calculates the width of every brick, according to other parameters of the game board.
     *
     * @param objectCount count of bricks in a row
     * @param rowLength   the total length of the row, in pixels
     * @return            the width of each brick
     */
    private static float calculateObjectWidth(float objectCount,
                                              float rowLength) {
        return (rowLength - ((objectCount - 1) * BRICK_SPACING)) / objectCount;
    }
}

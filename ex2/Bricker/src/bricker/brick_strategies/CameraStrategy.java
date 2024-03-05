package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.CameraController;
import bricker.main.BrickerGameManager;

/**
 * This strategy alters the camera position when the ball collides with the bricks
 * with this strategy assigned.
 * @author Nimrod M.
 */
public class CameraStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final Ball primaryBall;
    private final Vector2 windowDimensions;

    /**
     * Constructs a new CameraStrategy.
     *
     * @param gameManager      the game manager instance
     * @param brickGrid        the brick grid instance with the relevant bricks
     * @param primaryBall      the primary ball instance, only when the bricks
     *                         collide with this ball, the camera will be altered accordingly
     * @param windowDimensions the dimensions of the game window
     */
    public CameraStrategy(BrickerGameManager gameManager,
                          BrickGrid brickGrid,
                          Ball primaryBall,
                          Vector2 windowDimensions) {
        super(brickGrid);
        this.gameManager = gameManager;
        this.primaryBall = primaryBall;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Handles the collision between a brick and another object.
     *
     * @param object1 the first game object involved in the collision
     * @param object2 the second game object involved in the collision
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        // If the object colliding with the bricks is NOT the primary ball, 
        // then we shouldn't do anything
        if (object2 != primaryBall) {
            return;
        }

        // The camera is at a default position - it should be altered
        if (null == gameManager.camera()) {
            gameManager.setCamera(
                    new Camera(
                            primaryBall,
                            Vector2.ZERO,
                            windowDimensions.mult(1.2f),
                            windowDimensions));
            // Adding the camera controller, which takes responsibility on the camera
            // according to other events in the game
            gameManager.addGameObject(
                    new CameraController(
                        Vector2.ZERO,
                        Vector2.ZERO,
                        null,
                        gameManager,
                        primaryBall),
                    Layer.BACKGROUND);
        }
    }
}

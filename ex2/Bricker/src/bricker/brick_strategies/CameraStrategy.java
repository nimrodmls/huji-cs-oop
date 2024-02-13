package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.CameraController;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

public class CameraStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final Ball primaryBall;
    private final Vector2 windowDimensions;

    public CameraStrategy(BrickerGameManager gameManager,
                          BrickGrid brickGrid,
                          Ball primaryBall,
                          Vector2 windowDimensions,
                          Counter hitCounter) {
        super(brickGrid);
        this.gameManager = gameManager;
        this.primaryBall = primaryBall;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        // If the object colliding with the bricks is NOT the primary ball, then we shouldn't do anything
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

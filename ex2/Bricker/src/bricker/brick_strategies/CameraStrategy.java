package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

public class CameraStrategy extends BasicCollisionStrategy {
    /* Number of hits required for the camera to reset to default position.
       This number INCLUDES the first hit, which triggers the camera to move. */
    private static final int MAX_BRICKS_HIT_COUNT = 5;

    private final BrickerGameManager gameManager;
    private final Ball primaryBall;
    private final Vector2 windowDimensions;
    private final Counter hitCounter;

    public CameraStrategy(BrickerGameManager gameManager,
                          BrickGrid brickGrid,
                          Ball primaryBall,
                          Vector2 windowDimensions,
                          Counter hitCounter) {
        super(brickGrid);
        this.gameManager = gameManager;
        this.primaryBall = primaryBall;
        this.windowDimensions = windowDimensions;
        this.hitCounter = hitCounter;
    }

    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        // If the object colliding with the bricks is NOT the primary ball, then we shouldn't do anything
        if (object2 != primaryBall) {
            return;
        }

        // We reach here if the camera is at its default position
        if (0 == hitCounter.value()) {
            gameManager.setCamera(
                    new Camera(primaryBall, Vector2.ZERO, windowDimensions.mult(1.2f), windowDimensions));
        }

        hitCounter.increment();

        // If all the bricks were hit, and the camera should be reset
        if (MAX_BRICKS_HIT_COUNT == hitCounter.value()) {
            gameManager.setCamera(null);
            hitCounter.reset();
        }
    }
}

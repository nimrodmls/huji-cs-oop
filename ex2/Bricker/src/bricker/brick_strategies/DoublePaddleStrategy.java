package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.DoublePaddle;
import bricker.main.BrickerGameManager;
import bricker.utilities.GameConstants;

/**
 * Represents a collision strategy for a double paddle brick.
 * On collision, this strategy will spawn a new paddle for a limit amount of hits.
 * @author Nimrod M.
 */
public class DoublePaddleStrategy extends BasicCollisionStrategy {
    /**
     * These counters are used to keep track of the number of paddles and hits.
     * They are shared across all instances in order to keep the state.
     */
    private static final Counter paddleCounter = new Counter();
    private static final Counter hitCounter = new Counter();

    private final BrickerGameManager gameManager;
    private final UserInputListener inputListener;
    private final ImageRenderable paddleImage;
    private final Vector2 windowDimensions;

    /**
     * Constructs a new DoublePaddleStrategy.
     * This strategy creates a new paddle when the ball collides with the corresponding brick.
     *
     * @param gameManager      the game manager for the Bricker game
     * @param brickGrid        the brick grid for the Bricker game
     * @param inputListener    the user input listener for the Bricker game
     * @param paddleImage      the image renderable for the paddle
     * @param windowDimensions the dimensions of the game window
     * @param paddleCounter    the counter for the number of paddles
     * @param hitCounter       the counter for the number of hits
     */
    public DoublePaddleStrategy(BrickerGameManager gameManager,
                                BrickGrid brickGrid,
                                UserInputListener inputListener,
                                ImageRenderable paddleImage,
                                Vector2 windowDimensions) {
        super(brickGrid);
        this.gameManager = gameManager;
        this.inputListener = inputListener;
        this.paddleImage = paddleImage;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Handles the collision with the double paddle brick.
     * If there is already an active paddle, the collision is ignored.
     * Otherwise, a new paddle is created and provided to the player.
     *
     * @param object1 the first game object involved in the collision
     * @param object2 the second game object involved in the collision
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        if (0 != paddleCounter.value()) {
            // There is already a secondary paddle, ignore the collision
            return;
        }

        GameObject paddle =
                new DoublePaddle(
                        Vector2.ZERO,
                        GameConstants.PADDLE_DIMENSIONS,
                        windowDimensions,
                        paddleImage,
                        inputListener,
                        paddleCounter,
                        hitCounter,
                        gameManager);
        // We set the new paddle to be in the middle of the screen
        paddle.setCenter(windowDimensions.mult(0.5f));
        gameManager.addGameObject(paddle, Layer.FOREGROUND);
        paddleCounter.increment();
    }
}

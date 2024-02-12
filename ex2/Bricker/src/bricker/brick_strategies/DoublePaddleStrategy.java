package bricker.brick_strategies;

import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.DoublePaddle;
import bricker.main.BrickerGameManager;
import bricker.utilities.GameConstants;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class DoublePaddleStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final UserInputListener inputListener;
    private final ImageRenderable paddleImage;
    private final Vector2 windowDimensions;
    private final Counter paddleCounter;
    private final Counter hitCounter;

    public DoublePaddleStrategy(BrickerGameManager gameManager,
                                BrickGrid brickGrid,
                                UserInputListener inputListener,
                                ImageRenderable paddleImage,
                                Vector2 windowDimensions,
                                Counter paddleCounter,
                                Counter hitCounter) {
        super(brickGrid);
        this.gameManager = gameManager;
        this.inputListener = inputListener;
        this.paddleImage = paddleImage;
        this.windowDimensions = windowDimensions;
        this.paddleCounter = paddleCounter;
        this.hitCounter = hitCounter;
    }

    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        if (0 != paddleCounter.value()) {
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
        paddle.setCenter(
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2));
        gameManager.addGameObject(paddle, Layer.FOREGROUND);
        paddleCounter.increment();
    }
}

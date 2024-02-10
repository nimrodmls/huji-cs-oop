package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class DoublePaddle extends Paddle {

    private static final int MAX_HIT_COUNT = 4;

    private final BrickerGameManager gameManager;
    private final Counter paddleCounter;
    private Counter hitCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param windowDimensions
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    Receiving the input from the user, to control the Paddle.
     */
    public DoublePaddle(Vector2 topLeftCorner,
                        Vector2 dimensions,
                        Vector2 windowDimensions,
                        Renderable renderable,
                        UserInputListener inputListener,
                        Counter paddleCounter,
                        Counter hitCounter,
                        BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, windowDimensions, renderable, inputListener);
        this.paddleCounter = paddleCounter;
        this.hitCounter = hitCounter;
        this.gameManager = gameManager;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        hitCounter.increment();

        if (MAX_HIT_COUNT == hitCounter.value()) {
            hitCounter.reset();
            paddleCounter.reset();
            gameManager.removeGameObject(this, Layer.FOREGROUND);
        }
    }
}

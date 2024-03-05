package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import bricker.main.BrickerGameManager;

/**
 * The class represents a secondary paddle, additional to the primary paddle.
 * @author Nimrod M.
 */
public class DoublePaddle extends Paddle {

    // The maximum hit count the paddle can take before it is removed from the game
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
     * @param paddleCounter    The counter that keeps track of the number of secondary paddles in the game.
     * @param hitCounter       The counter that keeps track of the number of hits the paddle has taken.
     * @param gameManager      The game manager responsible for this object.
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

    /**
     * Called upon collision of the paddle with any other object.
     * Keeps track on the hits.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
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

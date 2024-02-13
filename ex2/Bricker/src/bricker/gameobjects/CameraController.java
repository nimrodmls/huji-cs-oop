package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import bricker.main.BrickerGameManager;

/**
 * The camera controller assumes control over the camera when activated.
 * The state of the camera will return to default once it has collided with a certain amount
 * of objects.
 * @author Nimrod M.
 */
public class CameraController extends GameObject  {
    private static final int MAX_HIT_COUNT = 4;

    private final BrickerGameManager gameManager;
    private final int initialHitCount;
    private final Ball ball;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param gameManager   The game manager responsible for this object.
     * @param ball          The ball that will be used to determine the camera's state.
     */
    public CameraController(Vector2 topLeftCorner,
                            Vector2 dimensions,
                            Renderable renderable,
                            BrickerGameManager gameManager,
                            Ball ball) {
        super(topLeftCorner, dimensions, renderable);
        this.gameManager = gameManager;
        this.ball = ball;
        initialHitCount = ball.getCollisionCounter();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Only when the ball has collided with a certain amount of objects, the camera will be
        // returned to its default state
        if (initialHitCount + MAX_HIT_COUNT <= ball.getCollisionCounter()) {
            gameManager.setCamera(null);
            gameManager.removeGameObject(this, Layer.BACKGROUND);
        }
    }
}

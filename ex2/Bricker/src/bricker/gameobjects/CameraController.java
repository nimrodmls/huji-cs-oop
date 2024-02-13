package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

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

        if (initialHitCount + MAX_HIT_COUNT <= ball.getCollisionCounter()) {
            gameManager.setCamera(null);
            gameManager.removeGameObject(this, Layer.BACKGROUND);
        }
    }
}

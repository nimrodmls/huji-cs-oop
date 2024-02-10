package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class FallingHeart extends GameObject {
    private final Paddle paddle;
    private final BrickerGameManager gameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Paddle paddle, BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.paddle = paddle;
        this.gameManager = gameManager;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == paddle;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameManager.addLife();
        gameManager.removeGameObject(this, Layer.DEFAULT);
    }
}

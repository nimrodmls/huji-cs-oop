package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import bricker.main.BrickerGameManager;

/**
 * Represents a falling heart game object that can collide with the user's paddle.
 * The falling heart will add a life to the player when it collides with the paddle.
 * @author Nimrod M.
 */
public class FallingHeart extends GameObject {
    private final Paddle paddle;
    private final BrickerGameManager gameManager;

    /**
     * Construct a new Falling Heart
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param paddle        The paddle that interacts with the falling heart.
     * @param gameManager   The game manager responsible for this object.
     */
    public FallingHeart(Vector2 topLeftCorner,
                        Vector2 dimensions,
                        Renderable renderable,
                        Paddle paddle,
                        BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.paddle = paddle;
        this.gameManager = gameManager;
    }

    /**
     * Validates whether the given object should collide with this object.
     *
     * @param other The game object colliding with this object.
     * @return True if the object should collide with the other object, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == paddle;
    }

    /**
     * Called upon collision of the falling heart with the paddle.
     * A life is added to the player.
     * 
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameManager.addLife();
        gameManager.removeGameObject(this, Layer.DEFAULT);
    }
}

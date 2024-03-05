package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball game object that can collide with other game objects.
 * @author Nimrod M.
 */
public class Ball extends GameObject {

    private final Sound collisionSound;
    private int collisionCounter = 0;

    /**
     * Constructs a new Ball instance.
     *
     * @param topLeftCorner Position of the ball, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height of the ball in window coordinates.
     * @param renderable    The renderable representing the ball. Can be null, in which case
     *                      the ball will not be rendered.
     * @param collisionSound The sound to be played when the ball collides with another object.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    /**
     * Called when the ball collides with another game object.
     *
     * @param other     The game object that the ball collided with.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionSound.play();
        collisionCounter++;
    }

    /**
     * Gets the number of collisions the ball has encountered.
     *
     * @return The collision counter.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }
}

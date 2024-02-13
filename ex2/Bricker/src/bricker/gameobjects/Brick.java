package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import bricker.brick_strategies.CollisionStrategy;

/**
 * Represents a brick on the game board. The brick is the target of the player,
 * and each brick on the board should be of this (or a derived) object.
 * Each brick has a collision strategy, which is applied when a collision occurs.
 *
 * @author Nimrod M.
 */
public class Brick extends GameObject {
    private CollisionStrategy collisionStrategy;

    /**
     * Construct a new Brick.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionStrategy the strategy applied to the brick.
     */
    public Brick(
            Vector2 topLeftCorner,
            Vector2 dimensions,
            Renderable renderable,
            CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Called upon collision of the brick with any other object.
     * The collision strategy is applied to the brick.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }
}

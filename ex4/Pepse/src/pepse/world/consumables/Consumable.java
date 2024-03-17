package pepse.world.consumables;

import java.util.function.BiConsumer;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A consumable object in the game.
 * Consumable objects are objects that can be consumed by the player's character.
 * Each consumable object has an energy value. The game manager can decide
 * what to do with that energy value.
 * @author Nimrod M.
 */
public abstract class Consumable extends GameObject {
    private final float energy;
    private final BiConsumer<Consumable, GameObject> onCollisionNotifier;

    /**
     * @param topLeftCorner The top left corner of the consumable object
     * @param dimensions The dimensions of the consumable object
     * @param renderable The renderable object that will be used to draw the consumable object
     * @param energy The energy value of the consumable object
     * @param onCollisionNotifier A function that will be called when the consumable
     *                            object collides with another object
     */
    public Consumable(Vector2 topLeftCorner,
                      Vector2 dimensions,
                      Renderable renderable,
                      float energy,
                      BiConsumer<Consumable, GameObject> onCollisionNotifier) {
        super(topLeftCorner, dimensions, renderable);
        this.energy = energy;
        this.onCollisionNotifier = onCollisionNotifier;
    }

    /**
     * Called when a collision occurs with the consumable.
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        onCollisionNotifier.accept(this, other);
    }

    /**
     * @return The energy value of the consumable object
     */
    public float getEnergy() {
        return energy;
    }
}

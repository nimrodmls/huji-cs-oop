package pepse.world.consumables;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.util.function.BiConsumer;

public abstract class Consumable extends GameObject {
    private final float energy;
    private final BiConsumer<Consumable, GameObject> onCollisionNotifier;

    public Consumable(Vector2 topLeftCorner,
                      Vector2 dimensions,
                      Renderable renderable,
                      float energy,
                      BiConsumer<Consumable, GameObject> onCollisionNotifier) {
        super(topLeftCorner, dimensions, renderable);
        this.energy = energy;
        this.onCollisionNotifier = onCollisionNotifier;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        onCollisionNotifier.accept(this, other);
    }

    public float getEnergy() {
        return energy;
    }
}

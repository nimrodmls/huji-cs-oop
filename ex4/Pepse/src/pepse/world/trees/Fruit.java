package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Fruit extends GameObject {

    private static final Color FRUIT_COLOR = Color.red;
    private final BiConsumer<GameObject, GameObject> onCollisionEnter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, BiConsumer<GameObject, GameObject> onCollisionEnter) {
        super(topLeftCorner, dimensions, new OvalRenderable(FRUIT_COLOR));
        this.onCollisionEnter = onCollisionEnter;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        onCollisionEnter.accept(this, other);
    }
}

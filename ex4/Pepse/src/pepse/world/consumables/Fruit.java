package pepse.world.consumables;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Fruit extends Consumable {

    private static final float FRUIT_ENERGY = 10.0f;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public Fruit(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Color color,
                 BiConsumer<Consumable, GameObject> onCollisionEnter) {
        super(topLeftCorner, dimensions, new OvalRenderable(color), FRUIT_ENERGY, onCollisionEnter);
    }

    public void setColor(Color color) {
        this.renderer().setRenderable(new OvalRenderable(color));
    }
}

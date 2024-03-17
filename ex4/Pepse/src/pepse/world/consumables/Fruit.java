package pepse.world.consumables;

import java.awt.*;
import java.util.function.BiConsumer;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

/**
 * A consumable fruit. The fruit has a color property, apart from the energy property.
 * @author Nimrod M.
 */
public class Fruit extends Consumable {

    private static final float FRUIT_ENERGY = 10.0f;

    /**
     * Construct a new Fruit instance.
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

    /**
     * Set the color of the fruit
     * @param color The new color of the fruit
     */
    public void setColor(Color color) {
        this.renderer().setRenderable(new OvalRenderable(color));
    }
}

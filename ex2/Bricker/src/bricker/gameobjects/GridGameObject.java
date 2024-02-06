package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class GridGameObject extends GameObject {
    private static final float DEFAULT_ELEMENT_SPACING = 3.0f;
    private final Vector2 dimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    X = Number of Rows, Y = Number of Columns.
     * @param elemHeight    Height of each element in the grid.
     * @param elemWidth     Width of each element in the grid.
     */
    public GridGameObject(
            Vector2 topLeftCorner, Vector2 dimensions, float elemHeight, float elemWidth) {
        super(
                topLeftCorner,
                new Vector2(
                    dimensions.x() * (elemHeight + DEFAULT_ELEMENT_SPACING),
                    dimensions.y() * (elemWidth + DEFAULT_ELEMENT_SPACING)),
                null);
        this.dimensions = dimensions;
    }

    public void addObject(int coordX, int coordY, GameObject object) {
        Vector2 initial = getTopLeftCorner();
        initial = initial.add(new Vector2(
                coordX * (object.getDimensions().x() + DEFAULT_ELEMENT_SPACING),
                coordY * (object.getDimensions().y() + DEFAULT_ELEMENT_SPACING)));
        object.setTopLeftCorner(new Vector2(
                getTopLeftCorner().x() + coordX * getDimensions().x(),
                getTopLeftCorner().y() + coordY * getDimensions().y()));
    }

    private static
}

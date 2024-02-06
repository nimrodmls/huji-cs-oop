package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class GridGameObject extends GameObject {
    
    private final Vector2 dimensions;
    private final Vector2 elementDimensions;
    private final float spacing;
    private final BrickerGameManager gameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner         Position of the object, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param dimensions            X = Number of Rows, Y = Number of Columns.
     * @param elementDimensions     X = Width of each element, Y = Height of each element.
     * @param spacing               Spacing between elements in pixels.
     * @param gameManager           The game manager that will manage this object.
     */
    public GridGameObject(
            Vector2 topLeftCorner, Vector2 dimensions, Vector2 elementDimensions, float spacing, BrickerGameManager gameManager) {
        super(
                topLeftCorner,
                new Vector2(
                    dimensions.x() * (elementDimensions.x() + spacing),
                    dimensions.y() * (elementDimensions.y() + spacing)),
                null);
        this.dimensions = dimensions;
        this.elementDimensions = elementDimensions;
        this.spacing = spacing;
        this.gameManager = gameManager;
    }

    public Vector2 getElementDimensions() {
        return elementDimensions;
    }

    public void addObject(int coordX, int coordY, GameObject object, int objectLayer) {
        Vector2 initial = getTopLeftCorner();
        // Fixing the element's dimensions to match the grid's
        object.setDimensions(elementDimensions);
        object.setTopLeftCorner(initial.add(new Vector2(
                coordX * (elementDimensions.x() + spacing),
                coordY * (elementDimensions.y() + spacing))));
        System.out.println(object.getTopLeftCorner());
        gameManager.addGameObject(object, objectLayer);
    }
}

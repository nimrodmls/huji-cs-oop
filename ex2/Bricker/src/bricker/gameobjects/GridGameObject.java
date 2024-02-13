package bricker.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;

import bricker.main.BrickerGameManager;

/**
 * Represents a generic grid. Objects can be added to the grid at specific coordinates,
 * they will be placed naturally within the specified boundaries of the grid.
 * @author Nimrod M.
 */
public class GridGameObject extends GameObject {
    
    private final Vector2 elementDimensions;
    private final float spacing;
    private final BrickerGameManager gameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner         Position of the object, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param dimensions            X = Number of Rows, Y = Number of Columns (NOT PIXELS!)
     * @param elementDimensions     X = Width of each element, Y = Height of each element (IN PIXELS!)
     * @param spacing               Spacing between elements in pixels.
     * @param gameManager           The game manager that will manage this object.
     */
    public GridGameObject(Vector2 topLeftCorner,
                          Vector2 dimensions,
                          Vector2 elementDimensions,
                          float spacing,
                          BrickerGameManager gameManager) {
        super(topLeftCorner,
              new Vector2(
                dimensions.x() * (elementDimensions.x() + spacing),
                dimensions.y() * (elementDimensions.y() + spacing)),
    null);
        this.elementDimensions = elementDimensions;
        this.spacing = spacing;
        this.gameManager = gameManager;
    }

    /**
     * Adds a object to the grid at the speicifed coordinates.
     *
     * @param coordX        X coordinate of the grid cell to place the object in.
     * @param coordY        Y coordinate of the grid cell to place the object in.
     * @param object        The object to add to the grid.
     * @param objectLayer   The layer to add the object to.
     */
    public void addObject(int coordX, int coordY, GameObject object, int objectLayer) {
        Vector2 initial = getTopLeftCorner();
        // Fixing the element's dimensions to match the grid's
        object.setDimensions(elementDimensions);
        object.setTopLeftCorner(initial.add(new Vector2(
                coordX * (elementDimensions.x() + spacing),
                coordY * (elementDimensions.y() + spacing))));
        gameManager.addGameObject(object, objectLayer);
    }
}

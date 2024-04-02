package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A block in the game. Blocks are immovable objects that can be used to create obstacles in the game.
 * @author Nimrod M.
 */
public class Block extends GameObject {

    /**
     * The size of a block in the game
     */
    public static final int BLOCK_SIZE = 30;

    /**
     * Construct a new block instance.
     * @param topLeftCorner The top left corner to spawn the block
     * @param renderable The renderable object that will be used to draw the block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(BLOCK_SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}

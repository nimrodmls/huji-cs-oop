package pepse.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

public class Terrain {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    private float groundHeightAtX0;
    private final Vector2 windowDimensions;

    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = windowDimensions.y() * (1.0f/3.0f);
        this.windowDimensions = windowDimensions;
    }

    public float groundHeightAt(float x) {
        return groundHeightAtX0;
    }

    public List<Block> createInRange(int minX, int maxX) {
        Renderable groundRenderable = new RectangleRenderable(
                ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        List<Block> blocks = new ArrayList<>();

        // Round the min and max x to the nearest block size
        float roundMinX = (float) Math.floor((double) minX / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;
        float roundMaxX = (float) Math.ceil((double) maxX / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;

        // Creating all the blocks
        for (float x = roundMinX; x < roundMaxX; x += Block.BLOCK_SIZE) {
            // The y value is the nearest multiple of the block size
            float yValue =
                    (float) Math.floor(groundHeightAt(x) / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;
            Block block = new Block(new Vector2(x, yValue), groundRenderable);
            blocks.add(block);
        }

        return blocks;
    }
}

package pepse.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.GameConstants;

import pepse.util.NoiseGenerator;

public class Terrain {

    private static final String TERRAN_BLOCK_TAG = "ground";
    private static final int TERRAIN_DEPTH = 20;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final double NOISE_FACTOR = Block.BLOCK_SIZE * 7;

    private final NoiseGenerator noiseGenerator;
    private final float groundHeightAtX0;

    public Terrain(Vector2 windowDimensions, double seed) {
        groundHeightAtX0 = (float) Math.floor(
                (windowDimensions.y() * GameConstants.INITIAL_GROUND_HEIGHT_FACTOR) /
                        Block.BLOCK_SIZE) * Block.BLOCK_SIZE;
        noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();

        // Round the min and max x to the nearest block size
        float roundMinX = (float) Math.floor((double) minX / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;
        float roundMaxX = (float) Math.ceil((double) maxX / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;

        // Creating all the blocks
        for (float x = roundMinX; x < roundMaxX; x += Block.BLOCK_SIZE) {
            // The maximum depth
            int maxDepth = (int)groundHeightAt(x) + (TERRAIN_DEPTH * Block.BLOCK_SIZE);

            for (float y = groundHeightAt(x); y < maxDepth; y += Block.BLOCK_SIZE) {
                Renderable groundRenderable = new RectangleRenderable(
                        ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(x, y), groundRenderable);
                block.setTag(TERRAN_BLOCK_TAG);
                blocks.add(block);
            }
        }

        return blocks;
    }
}

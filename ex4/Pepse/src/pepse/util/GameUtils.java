package pepse.util;

import pepse.world.Block;

import java.util.Random;

public class GameUtils {
    public static boolean biasedCoinFlip(float bias) {
        Random random = new Random();
        return random.nextFloat() < bias;
    }

    public static float lowerRoundToBlockSize(float x) {
        return (float) Math.floor((double) x / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;
    }

    public static float upperRoundToBlockSize(float x) {
        return (float) Math.ceil((double) x / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;
    }
}

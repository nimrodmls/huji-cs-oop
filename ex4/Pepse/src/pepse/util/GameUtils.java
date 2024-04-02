package pepse.util;

import java.util.Random;

import pepse.world.Block;

/**
 * General utility functions for the game with no specific category
 * @author Nimrod M.
 */
public class GameUtils {

    /**
     * @param bias The probability of returning true. If not in range [0,1], the result is undefined.
     * @return A biased coin flip.
     */
    public static boolean biasedCoinFlip(float bias) {
        Random random = new Random();
        return random.nextFloat() < bias;
    }

    /**
     * Rounding a number to the nearest multiple of Block.BLOCK_SIZE from below.
     * @param x The number to round.
     * @return The rounded x value.
     */
    public static float lowerRoundToBlockSize(float x) {
        return (float) Math.floor((double) x / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;
    }

    /**
     * Rounding a number to the nearest multiple of Block.BLOCK_SIZE from above.
     * @param x The number to round.
     * @return The rounded x value.
     */
    public static float upperRoundToBlockSize(float x) {
        return (float) Math.ceil((double) x / Block.BLOCK_SIZE) * Block.BLOCK_SIZE;
    }
}

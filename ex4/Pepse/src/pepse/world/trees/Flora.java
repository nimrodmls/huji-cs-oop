package pepse.world.trees;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.util.Dispatcher;
import pepse.util.GameConstants;
import pepse.world.consumables.Consumable;
import pepse.world.consumables.Fruit;
import pepse.util.GameUtils;

/**
 * Controlling all the flora in the game.
 * @author Nimrod M.
 */
public class Flora {
    private static final float TREE_SPAWN_CHANCE = 0.3f;
    private static final int TREE_DISTANCE = 3;
    private static final float JUMP_LEAF_ROTATION = 90.0f;
    private static final int BASE_STUMP_SIZE = 3;
    private static  final int MAX_EXTRA_STUMP_SIZE = 5;

    private final Function<Float, Float> treeRootSupplier;
    private final Consumer<GameObject> staticObjectManager;
    private final Consumer<GameObject> dynamicObjectManager;
    private final BiConsumer<Consumable, GameObject> treeFruitCollisionHandler;

    private final ArrayList<Tree> trees;
    private final LinkedList<Color> fruitColorCycle;
    private Color currentFruitColor = GameConstants.DEFAULT_FRUIT_COLOR;

    /**
     * Construct a new flora manager. No flora is spawned when this constructor is called.
     * @param treeRootSupplier The function that will be used to calculate the root of the tree
     * @param staticObjectManager The function that will be used to manage static objects
     *                            (objects that don't move)
     * @param dynamicObjectManager The function that will be used to manage dynamic objects
     *                             (objects that can move)
     * @param treeFruitCollisionHandler The function that will be used to handle collisions
     *                                  between trees and fruits
     * @param dispatcher The dispatcher that will be used to register events
     */
    public Flora(Function<Float, Float> treeRootSupplier,
                 Consumer<GameObject> staticObjectManager,
                 Consumer<GameObject> dynamicObjectManager,
                 BiConsumer<Consumable, GameObject> treeFruitCollisionHandler,
                 Dispatcher dispatcher) {

        this.treeRootSupplier = treeRootSupplier;
        this.staticObjectManager = staticObjectManager;
        this.dynamicObjectManager = dynamicObjectManager;
        this.treeFruitCollisionHandler = treeFruitCollisionHandler;

        dispatcher.registerListener(GameConstants.AVATAR_JUMP_EVENT, this::playerJumpCallback);

        trees = new ArrayList<>();

        fruitColorCycle = new LinkedList<>();
        fruitColorCycle.add(GameConstants.ALT_FRUIT_COLOR);
        fruitColorCycle.add(GameConstants.DEFAULT_FRUIT_COLOR);
    }

    /**
     * Creates trees in a given range.
     * Trees have a spawn chance, so it is not guaranteed that trees will be created.
     * @param minX The minimum x value for the range, it will be rounded to the nearest block size
     * @param maxX The maximum x value for the range, it will be rounded to the nearest block size
     * @return A list of all the trees created in the given range
     */
    public ArrayList<Tree> createInRange(int minX, int maxX) {

        // Round the min and max x to the nearest block size
        float roundMinX = GameUtils.lowerRoundToBlockSize(minX);
        float roundMaxX = GameUtils.upperRoundToBlockSize(maxX);

        Random random = new Random();
        // Creating all the trees - There's a chance to spawn a tree every TREE_DISTANCE blocks
        for (float x = roundMinX; x < roundMaxX; x += (Tree.STUMP_LINK_SIZE * TREE_DISTANCE)) {
            if (GameUtils.biasedCoinFlip(TREE_SPAWN_CHANCE)) {
                float y = treeRootSupplier.apply(x);
                Tree tree = new Tree(
                        random.nextInt(MAX_EXTRA_STUMP_SIZE) + BASE_STUMP_SIZE,
                        new Vector2(x, y),
                        currentFruitColor,
                        staticObjectManager,
                        dynamicObjectManager,
                        treeFruitCollisionHandler);
                trees.add(tree);
            }
        }

        return trees;
    }

    /**
     * Handles the event of the player jumping.
     * This method is called when the player jumps, and it changes the color of the fruits,
     * the color pattern of the stump and it rotates the leaves.
     * This method is called by the event dispatcher.
     */
    private void playerJumpCallback() {
        fruitColorCycle.add(currentFruitColor);
        currentFruitColor = fruitColorCycle.remove();

        for (Tree tree : trees) {
            tree.rotateLeaves(JUMP_LEAF_ROTATION);
            tree.resetStumpColor();

            for (Fruit fruit : tree.getFruits()) {
                fruit.setColor(currentFruitColor);
            }
        }

    }
}

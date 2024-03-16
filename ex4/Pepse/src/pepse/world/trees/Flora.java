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

public class Flora {
    private static final float TREE_SPAWN_CHANCE = 0.3f;
    private static final int TREE_DISTANCE = 3;

    private final Function<Float, Float> treeRootSupplier;
    private final Consumer<GameObject> staticObjectManager;
    private final Consumer<GameObject> dynamicObjectManager;
    private final BiConsumer<Consumable, GameObject> treeFruitCollisionHandler;

    private final ArrayList<Tree> trees;
    private final LinkedList<Color> fruitColorCycle;
    private Color currentFruitColor = GameConstants.DEFAULT_FRUIT_COLOR;

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
                        random.nextInt(5) + 3,
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

    private void playerJumpCallback() {
        fruitColorCycle.add(currentFruitColor);
        currentFruitColor = fruitColorCycle.remove();

        for (Tree tree : trees) {
            tree.rotateLeaves(90.0f);
            tree.resetStumpColor();

            for (Fruit fruit : tree.getFruits()) {
                fruit.setColor(currentFruitColor);
            }
        }

    }
}

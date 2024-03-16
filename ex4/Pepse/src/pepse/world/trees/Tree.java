package pepse.world.trees;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.consumables.Consumable;
import pepse.world.consumables.Fruit;
import pepse.util.GameUtils;

public class Tree {

    public static final int STUMP_LINK_SIZE = Block.BLOCK_SIZE;
    private static final int LEAF_SIZE = Block.BLOCK_SIZE;
    private static final Vector2 FRUIT_DIM = new Vector2(LEAF_SIZE / 2.0f, LEAF_SIZE / 2.0f);
    // Determines the size of the square of leaves around the top link of the stump
    private static final int LEAVES_DIMENSION = 5;
    private static final float MIN_LEAF_CYCLE_LENGTH = 5.0f;
    private static final float MAX_LEAF_CYCLE_LENGTH = 10.0f;
    private static final float MIN_LEAF_SCHED_START_TIME = 0.0f;
    private static final float MAX_LEAF_SCHED_START_TIME = 3.0f;
    private static final float LEAF_SPAWN_CHANCE = 0.7f;
    private static final float FRUIT_SPAWN_CHANCE = 0.3f;
    private static final Color BASE_STUMP_COLOR = new Color(100, 50, 20);
    private static final Color BASE_LEAVES_COLOR = new Color(50, 200, 30);
    private static final String LEAF_TAG = "leaf";
    private static final String FRUIT_TAG = "fruit";
    private static final String STUMP_TAG = "stump";

    private boolean inRotation = false;
    private final GameObject[] stumpLinks;
    private final ArrayList<GameObject> leaves;
    // The fruits on the tree, it's of dynamic size since it's not known in advance
    private final ArrayList<Fruit> fruits;

    public Tree(int stumpLength,
                Vector2 rootPosition,
                Color fruitColor,
                Consumer<GameObject> staticObjectManager,
                Consumer<GameObject> interactiveObjectManager,
                BiConsumer<Consumable, GameObject> fruitCollisionHandler) {

        // Creating the stump - It's essentially instances of Block in different color
        // The amount of stump links is constant and determined by the stump length
        stumpLinks = new GameObject[stumpLength];
        createStump(stumpLength, rootPosition, staticObjectManager);

        // The amount of fruits and leaves is not known in advance, so we use ArrayList
        leaves = new ArrayList<>();
        fruits = new ArrayList<>();
        // Creating the leaves - We create them as a square around the top link of the stump
        // each leaf has a chance of having a fruit
        createLeaves(
                rootPosition,
                stumpLength,
                fruitColor,
                staticObjectManager,
                interactiveObjectManager,
                fruitCollisionHandler);
    }

    public ArrayList<Fruit> getFruits() {
        return fruits;
    }

    public void resetStumpColor() {
        for (GameObject stumpLink : stumpLinks) {
            stumpLink.renderer().setRenderable(
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_STUMP_COLOR)));
        }
    }

    public void rotateLeaves(float rotationAngle) {
        for (GameObject leaf : leaves) {
            inRotation = true;
            new Transition<Float>(
                    leaf,
                    (Float angle) -> leaf.renderer().setRenderableAngle(angle),
                    leaf.renderer().getRenderableAngle(),
                    leaf.renderer().getRenderableAngle() + rotationAngle,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    1.0f,
                    Transition.TransitionType.TRANSITION_ONCE,
                    () -> inRotation = false);
        }
    }

    private void leafIdleRotationCallback(GameObject leaf, float angle) {
        // If the leaf is currently being rotated, the angle is not changed,
        // it will be changed once the leaf is gone idle
        if (inRotation) {
            return;
        }

        leaf.renderer().setRenderableAngle(angle);
    }

    private void createLeaves(
            Vector2 rootPosition,
            int stumpLength,
            Color fruitColor,
            Consumer<GameObject> staticObjectManager,
            Consumer<GameObject> interactiveObjectManager,
            BiConsumer<Consumable, GameObject> fruitCollisionHandler) {

        Random random = new Random();
        // Calculating where the stump of the tree ends
        Vector2 topLinkPos = rootPosition.subtract(new Vector2(0, stumpLength * STUMP_LINK_SIZE));

        // Creating all the leaves - The leaves are a square around the top link of the stump
        for (int row = -(LEAVES_DIMENSION / 2); row < (LEAVES_DIMENSION / 2) + 1; row++) {
            for (int col = -(LEAVES_DIMENSION / 2); col < (LEAVES_DIMENSION / 2) + 1; col++) {

                // There is a 70% chance of a leaf being created
                if (GameUtils.biasedCoinFlip(1.0f - LEAF_SPAWN_CHANCE)) {
                    continue;
                }

                Vector2 pos = topLinkPos.add(new Vector2(row * LEAF_SIZE, col * LEAF_SIZE));
                GameObject leaf = createLeafObject(pos);
                // The leaf's movement is delayed by a random amount of time
                new ScheduledTask(
                        leaf,
                        random.nextFloat(MIN_LEAF_SCHED_START_TIME, MAX_LEAF_SCHED_START_TIME),
                        false,
                        () -> createLeafTransitions(leaf));
                staticObjectManager.accept(leaf);
                leaves.add(leaf);

                // Possibly adding a fruit to the leaf - There is 30% chance of a fruit being added
                if (GameUtils.biasedCoinFlip(FRUIT_SPAWN_CHANCE)) {
                    Fruit fruit = createFruitObject(
                            leaf.getCenter(),
                            fruitColor,
                            fruitCollisionHandler);
                    fruit.setCenter(leaf.getCenter());
                    interactiveObjectManager.accept(fruit);
                    fruits.add(fruit);
                }
            }
        }
    }

    private void createStump(int stumpLength, Vector2 rootPosition, Consumer<GameObject> staticObjectManager) {
        for (int i = 0; i < stumpLength; i++) {
            Block stump = new Block(
                    rootPosition.subtract(new Vector2(0, i * STUMP_LINK_SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_STUMP_COLOR)));
            staticObjectManager.accept(stump);
            stumpLinks[i] = stump;
            stump.setTag(STUMP_TAG);
        }
    }

    // Associates the given leaf with the super realistic moving transitions
    private void createLeafTransitions(GameObject leaf) {
        Random random = new Random();
        new Transition<Float>(
                leaf,
                (Float angle) -> leafIdleRotationCallback(leaf, angle),
                0.0f,
                60.0f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                random.nextFloat(MIN_LEAF_CYCLE_LENGTH, MAX_LEAF_CYCLE_LENGTH),
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        new Transition<Vector2>(
                leaf,
                leaf::setDimensions,
                new Vector2(LEAF_SIZE, LEAF_SIZE),
                new Vector2(LEAF_SIZE, LEAF_SIZE).mult(0.5f),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                random.nextFloat(MIN_LEAF_CYCLE_LENGTH, MAX_LEAF_CYCLE_LENGTH),
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    private static GameObject createLeafObject(Vector2 position) {
        GameObject leaf = new GameObject(
                position,
                new Vector2(LEAF_SIZE, LEAF_SIZE),
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAVES_COLOR)));
        leaf.setTag(LEAF_TAG);
        return leaf;
    }

    private static Fruit createFruitObject(Vector2 position,
                                           Color fruitColor,
                                           BiConsumer<Consumable, GameObject> fruitCollisionHandler) {
        Fruit fruit = new Fruit(
                position,
                FRUIT_DIM,
                fruitColor,
                fruitCollisionHandler);
        fruit.setTag(FRUIT_TAG);
        return fruit;
    }
}

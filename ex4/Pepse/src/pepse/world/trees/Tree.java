package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.consumables.Consumable;
import pepse.world.consumables.Fruit;

import java.awt.*;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Tree {

    private static final int STUMP_LINK_SIZE = 30;
    private static final int LEAF_SIZE = 30;
    // Determines the size of the square of leaves around the top link of the stump
    private static final int LEAVES_DIMENSION = 5;
    private static final float MIN_LEAF_CYCLE_LENGTH = 5.0f;
    private static final float MAX_LEAF_CYCLE_LENGTH = 10.0f;
    private static final float MIN_LEAF_SCHED_START_TIME = 0.0f;
    private static final float MAX_LEAF_SCHED_START_TIME = 3.0f;
    private static final Color BASE_STUMP_COLOR = new Color(100, 50, 20);
    private static final Color BASE_LEAVES_COLOR = new Color(50, 200, 30);

    public Tree(int stumpLength,
                Vector2 rootPosition,
                Consumer<GameObject> staticObjectManager,
                Consumer<GameObject> interactiveObjectManager,
                BiConsumer<Consumable, GameObject> fruitCollisionHandler) {

        // Creating the stump - It's essentially instances of Block in different color
        for (int i = 0; i < stumpLength; i++) {
            Block stump = new Block(
                    rootPosition.subtract(new Vector2(0, i * STUMP_LINK_SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_STUMP_COLOR)));
            staticObjectManager.accept(stump);
        }

        // Creating the leaves - We create them as a square around the top link of the stump
        Random random = new Random();
        Vector2 topLinkPos = rootPosition.subtract(new Vector2(0, stumpLength * STUMP_LINK_SIZE));
        for (int i = -(LEAVES_DIMENSION / 2); i < (LEAVES_DIMENSION / 2) + 1; i++) {
            for (int j = -(LEAVES_DIMENSION / 2); j < (LEAVES_DIMENSION / 2) + 1; j++) {
                Vector2 pos = topLinkPos.add(new Vector2(i * LEAF_SIZE, j * LEAF_SIZE));
                GameObject leaf = new GameObject(
                        pos,
                        new Vector2(LEAF_SIZE, LEAF_SIZE),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAVES_COLOR)));
                new ScheduledTask(
                        leaf,
                        random.nextFloat(MIN_LEAF_SCHED_START_TIME, MAX_LEAF_SCHED_START_TIME),
                        false,
                        () -> createLeafTransitions(leaf));
                staticObjectManager.accept(leaf);

                // Possibly adding a fruit to the leaf - There is 30% chance of a fruit being added
                float randFloat = random.nextFloat(0.0f, 1.0f);
                if (randFloat <= 0.3f) {
                    Fruit fruit = new Fruit(pos, new Vector2(LEAF_SIZE / 2.0f, LEAF_SIZE / 2.0f), fruitCollisionHandler);
                    fruit.setCenter(leaf.getCenter());
                    interactiveObjectManager.accept(fruit);
                }
            }
        }
    }

    // Associates the given leaf with the super realistic moving transitions
    private static void createLeafTransitions(GameObject leaf) {
        Random random = new Random();
        new Transition<Float>(
                leaf,
                (Float angle) -> leaf.renderer().setRenderableAngle(angle),
                0.0f,
                360.0f,
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
}

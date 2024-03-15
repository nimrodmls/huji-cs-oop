package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.function.Consumer;

public class Tree {

    private static final int STUMP_LINK_SIZE = 30;
    private static final int LEAF_SIZE = 30;
    // Determines the size of the square of leaves around the top link of the stump
    private static final int LEAVES_DIMENSION = 5;
    private static final float LEAF_CYCLE_LENGTH = 5.0f;
    private static final Color BASE_STUMP_COLOR = new Color(100, 50, 20);
    private static final Color BASE_LEAVES_COLOR = new Color(50, 200, 30);

    public Tree(int stumpLength, Vector2 rootPosition, Consumer<GameObject> objectManager) {
        // Creating the stump - It's essentially instances of Block in different color
        for (int i = 0; i < stumpLength; i++) {
            Block stump = new Block(
                    rootPosition.subtract(new Vector2(0, i * STUMP_LINK_SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_STUMP_COLOR)));
            objectManager.accept(stump);
        }

        // Creating the leaves - We create them as a square around the top link of the stump
        Vector2 topLinkPos = rootPosition.subtract(new Vector2(0, stumpLength * STUMP_LINK_SIZE));
        for (int i = -(LEAVES_DIMENSION / 2); i < (LEAVES_DIMENSION / 2) + 1; i++) {
            for (int j = -(LEAVES_DIMENSION / 2); j < (LEAVES_DIMENSION / 2) + 1; j++) {
                Vector2 pos = topLinkPos.add(new Vector2(i * LEAF_SIZE, j * LEAF_SIZE));
                Vector2 baseSize = new Vector2(LEAF_SIZE, LEAF_SIZE);
                GameObject leaf = new GameObject(
                        pos,
                        baseSize,
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAVES_COLOR)));
                new Transition<Float>(
                        leaf,
                        (Float angle) -> leaf.renderer().setRenderableAngle(angle),
                        0.0f,
                        360.0f,
                        Transition.LINEAR_INTERPOLATOR_FLOAT,
                        LEAF_CYCLE_LENGTH,
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                        null);
                new Transition<Vector2>(
                        leaf,
                        leaf::setDimensions,
                        baseSize,
                        baseSize.mult(0.8f),
                        Transition.LINEAR_INTERPOLATOR_VECTOR,
                        LEAF_CYCLE_LENGTH,
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                        null);
                objectManager.accept(leaf);
            }
        }
    }
}

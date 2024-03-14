package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.function.Consumer;

public class Tree {

    private static final int STUMP_LINK_SIZE = 30;
    private static final Color BASE_STUMP_COLOR = new Color(100, 50, 20);
    private static final Color BASE_LEAVES_COLOR = new Color(50, 200, 30);

    public Tree(int stumpLength, Vector2 rootPosition, Consumer<GameObject> objectManager) {
        for (int i = 0; i < stumpLength; i++) {
            Vector2 pos = rootPosition.subtract(new Vector2(0, i * STUMP_LINK_SIZE));
            GameObject stump = new GameObject(
                    // We subtract since the trees go up, and the y axis increases downwards
                    pos,
                    new Vector2(STUMP_LINK_SIZE, STUMP_LINK_SIZE),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_STUMP_COLOR)));
            objectManager.accept(stump);
        }
    }
}

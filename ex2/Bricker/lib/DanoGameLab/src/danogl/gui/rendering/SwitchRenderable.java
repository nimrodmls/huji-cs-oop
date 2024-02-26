package danogl.gui.rendering;

import danogl.components.Component;
import danogl.components.SwitchComponent;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

/**
 * A Renderable that switches between other Renderables based on given conditions
 * and which of them hold in a given frame
 * @author Dan Nirel
 */
public class SwitchRenderable implements Renderable {
    private final SwitchComponent switchComponent;
    private Renderable renderable;

    /**
     * Construct a SwitchRenderable.
     * @param conditions a list of non-null BooleanSuppliers. If any returns true
     *                   in a given frame, the corresponding Renderable will be rendered.
     *                   Subsequent conditions and renderables will not be checked.
     * @param renderables a list of Renderables corresponding to the conditions list.
     *                    these may be null. If a null renderable's condition is true,
     *                    nothing will be rendered.
     * @param defaultRenderable if none of the conditions held, this Renderable will
     *                          be used in this frame. May be null.
     */
    public SwitchRenderable(List<BooleanSupplier> conditions, List<Renderable> renderables,
                            Renderable defaultRenderable) {
        //use the SwitchComponent to only set the renderable field to the desired Renderable
        switchComponent = new SwitchComponent(
                conditions,
                renderables.stream().<Component>map(
                        rend->(deltaTime -> renderable = rend)).collect(Collectors.toList()),
                deltaTime -> renderable = defaultRenderable, true);
    }

    /**
     * Construct a simple switch with only two Renderable options. If the given
     * condition (non-null) is true in a given frame, renderableIfHolds is rendered,
     * otherwise renderableIfNotHolds is rendered.
     */
    public SwitchRenderable(BooleanSupplier condition, Renderable renderableIfHolds,
                            Renderable renderableIfNotHolds) {
        this(List.of(condition), Collections.singletonList(renderableIfHolds), renderableIfNotHolds);
    }

    @Override
    public void render(Graphics2D g, Vector2 topLeftCorner,
                       Vector2 dimensions, double degreesCounterClockwise,
                       boolean isFlippedHorizontally,
                       boolean isFlippedVertically, double opaqueness) {
        switchComponent.update(0); //sets renderable to the appropriate one
        if(renderable == null)
            return;
        renderable.render(g, topLeftCorner, dimensions, degreesCounterClockwise,
                isFlippedHorizontally, isFlippedVertically, opaqueness);
    }
}

package danogl.components.movement_schemes;

import danogl.GameObject;
import danogl.components.movement_schemes.movement_directing.CommonMovementActions;
import danogl.components.movement_schemes.movement_directing.MovementDirector;
import danogl.util.Vector2;

/**
 * MovementScheme for "dragging" an object, i.e. controlling its position precisely.
 * Most commonly the mouse would be used to drag the object, though any
 * MovementDirector would do as long as it supplies a vector value for the action
 * CommonMovementActions.DRAG_POS_VECTOR.
 * @author Dan Nirel
 */
public class DragMovementScheme extends MovementScheme {
    ////private static final float MAX_DRAG_VELOCITY = 2500;
    private final boolean maintainVelocity;
    private Vector2 deltaToCenter = Vector2.ZERO;
    private boolean isDragging = false;
    private Vector2 prevPos;

    public DragMovementScheme(GameObject gameObject,
                              MovementDirector movementDirector, boolean maintainDragVelocityAfterDrag) {
        super(gameObject, movementDirector);
        this.maintainVelocity = maintainDragVelocityAfterDrag;
    }

    public DragMovementScheme(GameObject gameObject, MovementDirector movementDirector) {
        this(gameObject, movementDirector, false);
    }

    @Override
    public void update(float deltaTime) {
        if(!movementDirector.getActionValue(CommonMovementActions.IS_DRAGGING_BOOL, false)) {
            isDragging = false;
            return;
        }
        //then should drag, if object contains drag point
        Vector2 dragPos = movementDirector.getActionValue(CommonMovementActions.DRAG_POS_VECTOR, Vector2.ZERO);
        if(!isDragging && gameObject.containsPoint(dragPos)) {
            //then this is the first frame in which to drag.
            //record delta to center and start dragging.
            deltaToCenter = dragPos.subtract(gameObject.getCenter());
            prevPos = gameObject.getCenter();
            isDragging = true;
        }
        //either if dragging from now or from previous frames:
        if(isDragging) {
            gameObject.setCenter(dragPos.subtract(deltaToCenter));
            gameObject.setVelocity(Vector2.ZERO);
        }
    }

    public boolean isDragging() { return isDragging; }
}

package danogl.components.movement_schemes;

import danogl.GameObject;
import danogl.components.movement_schemes.movement_directing.CommonMovementActions;
import danogl.components.movement_schemes.movement_directing.MovementDirector;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

/**
 * The GameObject will move towards a supplied (movable) target location, when
 * a given predicate holds.
 * Specialized methods are available for when the target is the mouse location.
 * <br>Use case: move an avatar towards the mouse if the right mouse button is pressed.
 * @author Dan Nirel
 */
public class TargetMovementScheme extends MovementScheme {
    private Vector2 target;
    private boolean advancing = false;

    public TargetMovementScheme(
            GameObject gameObject,
            MovementDirector movementDirector,
            float velocity) {
        super(gameObject, movementDirector, velocity);
        target = gameObject.getCenter();
    }

    public TargetMovementScheme(
            GameObject gameObject,
            MovementDirector movementDirector) {
        super(gameObject, movementDirector, DEFAULT_VELOCITY);
    }

    @Override
    public void update(float deltaTime) {
        if(movementDirector.getActionValue(CommonMovementActions.START_ADVANCING_TO_TARGET_BOOL, false)) {
            advancing = true;
            return;
        }

        if(!advancing || movementDirector.getActionValue(
                CommonMovementActions.STOP_ADVANCING_TO_TARGET_BOOL, false)) {
            advancing = false;
            gameObject.setVelocity(Vector2.ZERO);
            return;
        }

        //then is advancing
        target = movementDirector.getActionValue(CommonMovementActions.TARGET_VECTOR, gameObject.getCenter());
        MutableVector2 velocity = new MutableVector2(target);
        velocity.selfSubtract(gameObject.getCenter());
        if(velocity.magnitude() < velocitySize() * deltaTime) {
            gameObject.setVelocity(Vector2.ZERO);
            return;
        }
        velocity.selfNormalize().selfMult(velocitySize());
        gameObject.transform().setVelocity(velocity);
    }
}

package danogl.components.movement_schemes;

import danogl.GameObject;
import danogl.components.movement_schemes.movement_directing.CommonMovementActions;
import danogl.components.movement_schemes.movement_directing.MovementDirector;
import danogl.util.Vector2;

/**
 * MovementScheme that allows the GameObject to move sideways.
 * Specialized methods are available for when this should be determined by the keyboard.
 * <br>Use case: an AI agent or a user-avatar that only moves sideways.
 * @author Dan Nirel
 */
public class RailMovementScheme extends MovementScheme {
    private final Vector2 railDirection;

    public RailMovementScheme(
            GameObject gameObject,
            MovementDirector movementDirector,
            Vector2 railDirection,
            float velocity) {
        super(gameObject, movementDirector, velocity);
        this.railDirection = railDirection;
    }

    public RailMovementScheme(
            GameObject gameObject,
            MovementDirector movementDirector,
            Vector2 railDirection) {
        this(gameObject, movementDirector, railDirection, DEFAULT_VELOCITY);
    }

    public RailMovementScheme(
            GameObject gameObject,
            MovementDirector movementDirector) {
        this(gameObject, movementDirector, Vector2.RIGHT);
    }

    @Override
    public void update(float deltaTime) {
        var unrestrainedMovement = movementDirector.getActionValue(
                CommonMovementActions.MOVEMENT_VECTOR, Vector2.ZERO);
        float linearVel = Math.signum(unrestrainedMovement.dot(railDirection)) * velocitySize();
        gameObject.setVelocity(railDirection.mult(linearVel));
    }
}

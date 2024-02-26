package danogl.components.movement_schemes;

import danogl.GameObject;
import danogl.components.movement_schemes.movement_directing.CommonMovementActions;
import danogl.components.movement_schemes.movement_directing.MovementDirector;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

/**
 * MovementScheme that allows the GameObject to move sideways, up, down, or in any of the diagonals.
 * Specialized methods are available for when this should be determined by the keyboard.
 * Further specialized methods are available for when the arrow-keys specifically
 * determine movement, and for when WASD keys do so.
 * <br>This is an extension of {@link RailMovementScheme}.
 * @author Dan Nirel
 */
public class DirectionalMovementScheme extends MovementScheme {
    public DirectionalMovementScheme(
            GameObject gameObject,
            MovementDirector movementDirector,
            float velocity) {
        super(gameObject, movementDirector, velocity);
    }

    public DirectionalMovementScheme(GameObject gameObject, MovementDirector movementDirector) {
        super(gameObject, movementDirector);
    }

    @Override
    public void update(float deltaTime) {
        MutableVector2 velocityVec = new MutableVector2(
                movementDirector.getActionValue(CommonMovementActions.MOVEMENT_VECTOR, Vector2.ZERO));
        velocityVec.selfMult(velocitySize());
        gameObject.setVelocity(velocityVec);
    }
}
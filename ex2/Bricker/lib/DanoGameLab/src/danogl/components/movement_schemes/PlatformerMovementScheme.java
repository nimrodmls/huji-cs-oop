package danogl.components.movement_schemes;

import danogl.GameObject;
import danogl.components.movement_schemes.movement_directing.CommonMovementActions;
import danogl.components.movement_schemes.movement_directing.MovementDirector;

/**
 * MovementScheme that allows a GameObject to move sideways, and jump if it isn't already jumping.
 * Specialized methods are available for when this should be determined by the keyboard.
 * <br>Functionally, this is an extension of {@link RailMovementScheme}.
 * <br>Use case: an AI agent or a user-avatar in a platformer game.
 * <br>Note that under normal use the GameObject should also have "gravity" turned on
 * using {@link danogl.components.Transform#setAccelerationY(float)}, for example
 * myGameObject.transform().setAccelerationY(600) .
 * @author Dan Nirel
 */
public class PlatformerMovementScheme extends MovementScheme {
    /**Default jump velocity*/
    public static final float DEFAULT_JUMP_VELOCITY = -650;

    private float jumpVelocity;

    public PlatformerMovementScheme(
            GameObject gameObject, MovementDirector movementDirector,
            float horizontalVelocity, float jumpVelocity) {
        super(gameObject, movementDirector, horizontalVelocity);
        this.jumpVelocity = jumpVelocity;
    }

    public PlatformerMovementScheme(
            GameObject gameObject, MovementDirector movementDirector) {
        this(gameObject, movementDirector, DEFAULT_VELOCITY, DEFAULT_JUMP_VELOCITY);
    }

    /**Get the jump velocity*/
    public float jumpVelocity() { return jumpVelocity; }
    /**Set the jump velocity*/
    public void setJumpVelocity(float jumpVelocity) { this.jumpVelocity = jumpVelocity; }

    @Override
    public void update(float deltaTime) {
        float xVel = movementDirector
                .getActionValue(CommonMovementActions.HORIZONTAL_MOVEMENT_FLOAT, 0f);
        gameObject.transform().setVelocityX(velocitySize() * xVel);

        if(movementDirector.getActionValue(CommonMovementActions.IS_STARTING_JUMP_BOOL, false)
                && gameObject.getVelocity().y() == 0) {
            gameObject.transform().setVelocityY(jumpVelocity);
        }
    }
}

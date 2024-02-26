package danogl.components.movement_schemes;

import danogl.GameObject;
import danogl.components.RendererComponent;
import danogl.components.movement_schemes.movement_directing.CommonMovementActions;
import danogl.components.movement_schemes.movement_directing.MovementDirector;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

/**
 * MovementScheme that allows the GameObject to turn (rotate) left and right, and move forwards
 * or backwards.
 * Specialized methods are available for when this should be determined by the keyboard.
 * <br>Use case: an avatar similar to that of GTA 1 or 2. When used creatively using suitable
 * predicates (namely, always move forwards) and using the getter/setter for movement speed,
 * the MovementScheme can also simulate an Asteroids-like spaceship that can turn and accelerate
 * forwards.
 * @author Dan Nirel
 */
public class SteeringMovementScheme extends MovementScheme {
    /**For when a backwards velocity is not specified*/
    public static final float DEFAULT_REVERSE_VELOCITY = DEFAULT_VELOCITY/2;
    /**For when an angular velocity is not specified*/
    public static final float DEFAULT_ANGULAR_VELOCITY = 260;

    private final RendererComponent rendererToRotate;
    private float angularVelocity;
    private float reverseVelocity = DEFAULT_REVERSE_VELOCITY;
    private MutableVector2 forwards = new MutableVector2(Vector2.UP);

    public SteeringMovementScheme(GameObject gameObject, MovementDirector movementDirector,
                                  RendererComponent rendererToRotate,
                                  float movementVelocity, float angularVelocity) {
        super(gameObject, movementDirector, movementVelocity);
        this.rendererToRotate = rendererToRotate;
        this.angularVelocity = angularVelocity;
    }

    public SteeringMovementScheme(GameObject gameObject, MovementDirector movementDirector,
                                  RendererComponent rendererToRotate) {
        this(gameObject, movementDirector, rendererToRotate, DEFAULT_VELOCITY, DEFAULT_ANGULAR_VELOCITY);
    }

    public SteeringMovementScheme(GameObject gameObject, MovementDirector movementDirector) {
        this(gameObject, movementDirector, gameObject.renderer(), DEFAULT_VELOCITY, DEFAULT_ANGULAR_VELOCITY);
    }

    /**Current angular velocity*/
    public float angularVelocity() {
        return angularVelocity;
    }

    /**Current backwards velocity*/
    public float reverseVelocity() {
        return reverseVelocity;
    }

    /**Set the current backwards velocity*/
    public void setReverseVelocity(float reverseVelocity) {
        this.reverseVelocity = reverseVelocity;
    }

    /**Set the current angular velocity*/
    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    /**The vector currently considered forwards*/
    public MutableVector2 forwardsDir() {
        return forwards;
    }

    /**Set the vector currently considered forwards*/
    public void setForwardsDir(Vector2 forwards) {
        this.forwards.setXY(forwards);
    }

    @Override
    public void update(float deltaTime) {
        float turnAmount = movementDirector.getActionValue(
                CommonMovementActions.ROTATION_DIRECTION_FLOAT, 0f);
        float deltaAngle = angularVelocity * turnAmount * deltaTime;

        if(rendererToRotate != null)
            rendererToRotate.setRenderableAngle(rendererToRotate.getRenderableAngle()+deltaAngle);

        forwards.selfRotate(deltaAngle);

        float forwardsAmount = movementDirector.getActionValue(
                CommonMovementActions.FORWARDS_BACKWARDS_FLOAT, 0f);
        float vel = forwardsAmount * (forwardsAmount > 0?velocitySize():reverseVelocity());
        gameObject.transform().setVelocity(forwards.mult(vel));
    }
}

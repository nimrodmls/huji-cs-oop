package danogl.components.movement_schemes;

import danogl.GameObject;
import danogl.components.Component;
import danogl.components.movement_schemes.movement_directing.MovementDirector;

/**
 * Note than this is only an abstract superclass for concrete MovementSchemes.
 * <br>
 * A MovementScheme component is meant to move a GameObject according to a certain scheme.
 * Usually, a specific MovementScheme is designed for a certain kind of movement, such as drag-and-drop,
 * one-dimensional movement and so on. Choose a concrete MovementScheme to use from this package or create
 * your own.
 * <br>
 * The movement type, which dictates the MovementScheme chosen, is unrelated to what/who decides when and how
 * to actually move. For example, the MovementScheme may dictate that the GameObject can move only on a rail,
 * but it does not decide itself when to move and in what velocity. This is decided by the MovementDirector
 * which is supplied to the MovementScheme. The MovementDirector tells the MovementScheme which of the movements
 * the MovementScheme supports are to be actually executed in a given frame. The MovementDirector sent to the
 * MovementScheme can be based on user-input or by programming logic which is the brain of the GameObject.
 * <br>
 * Usually at most one MovementScheme will be assigned to a GameObject.
 * @author Dan Nirel
 */
public abstract class MovementScheme implements Component {
    /**For when a movement velocity is not specified*/
    public static final float DEFAULT_VELOCITY = 400;

    protected final GameObject gameObject;
    protected final MovementDirector movementDirector;
    private float velocity;

    /**
     * Constructs a new MovementScheme.
     * @param gameObject The GameObject controlled by this MovementScheme. Note that as a Component,
     *                   the MovementScheme needs to be updated, usually by a GameObject that contains
     *                   the MovementScheme as a component. The GameObject updating this MovementScheme
     *                   and the GameObject that is moved by this MovementScheme may or may not be the same,
     *                   though they usually would be. In normal use, you would both send a GameObject
     *                   to the constructor, and additionally add the constructed MovementScheme as a
     *                   component to the same GameObject using GameObject.addComponent.
     * @param movementDirector The brain of the MovementScheme, deciding if and when to tell the MovementScheme
     *                         to move the GameObject according to the MovementScheme's supported scheme.
     * @param velocity Most MovementSchemes have a dominant velocity, even if it is not used in every
     *                 movement aspect. The MovementScheme may decide to store that velocity in this
     *                 superclass.
     */
    public MovementScheme(
            GameObject gameObject,
            MovementDirector movementDirector,
            float velocity) {
        this.gameObject = gameObject;
        this.movementDirector = movementDirector;
        this.velocity = velocity;
    }

    /**
     * Constructs a new MovementScheme. The MovementScheme's movement velocity (if relevant)
     * is set to be DEFAULT_VELOCITY.
     * @param gameObject The GameObject controlled by this MovementScheme. Note that as a Component,
     *                   the MovementScheme needs to be updated, usually by a GameObject that contains
     *                   the MovementScheme as a component. The GameObject updating this MovementScheme
     *                   and the GameObject that is moved by this MovementScheme may or may not be the same,
     *                   though they usually would be. In normal use, you would both send a GameObject
     *                   to the constructor, and additionally add the constructed MovementScheme as a
     *                   component to the same GameObject using GameObject.addComponent.
     * @param movementDirector The brain of the MovementScheme, deciding if and when to tell the MovementScheme
     *                         to move the GameObject according to the MovementScheme's supported scheme.
     */
    public MovementScheme(
            GameObject gameObject,
            MovementDirector movementDirector) {
        this(gameObject, movementDirector, DEFAULT_VELOCITY);
    }

    /**Returns the currently set velocity*/
    public float velocitySize() { return velocity; }

    /**Sets the velocity*/
    public void setVelocitySize(float velocity) {
        this.velocity = velocity;
    }
}

package danogl.components.movement_schemes.movement_directing;

import danogl.gui.MessageHandler;
import danogl.gui.UserInputListener;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * A MovementDirector that responds to keyboard input.
 * In the context of this MovementDirector it is important to distinguish between
 * input functions such as "up" or "jump", to their assigned keys.
 * An instance of this class is already constructed with limited builtin input functions.
 * The keys for these can be altered,
 * and additional input functions (with their keys) can be added.
 * @author Dan Nirel
 */
public class KeyboardMovementDirector extends MovementDirector {
    /**The input function "up". Assigned the up arrow by default*/
    public static final String INPUT_UP    = "up";
    /**The input function "down". Assigned the down arrow by default*/
    public static final String INPUT_DOWN  = "down";
    /**The input function "left". Assigned the left arrow by default*/
    public static final String INPUT_LEFT  = "left";
    /**The input function "right". Assigned the right arrow by default*/
    public static final String INPUT_RIGHT = "right";
    /**The input function "jump". Assigned the space key by default*/
    public static final String INPUT_JUMP  = "jump";

    private final UserInputListener inputListener;
    private Map<String, Integer> keyboardKeyMap = new HashMap<>(Map.of(
            INPUT_LEFT, KeyEvent.VK_LEFT,
            INPUT_RIGHT, KeyEvent.VK_RIGHT,
            INPUT_DOWN, KeyEvent.VK_DOWN,
            INPUT_UP, KeyEvent.VK_UP,
            INPUT_JUMP, KeyEvent.VK_SPACE
    ));

    /**
     * Constructs the KeyboardMovementDirector.
     * @param messages A MessageHandler to log error messages with
     * @param inputListener to read input through
     */
    public KeyboardMovementDirector(MessageHandler messages, UserInputListener inputListener) {
        super(messages);
        this.inputListener = inputListener;
        this.registerAction(CommonMovementActions.HORIZONTAL_MOVEMENT_FLOAT, this::horizontalMovementSupplier);
        this.registerAction(CommonMovementActions.VERTICAL_MOVEMENT_FLOAT, this::verticalMovementSupplier);
        this.registerAction(CommonMovementActions.MOVEMENT_VECTOR, this::movementSupplier);
        this.registerAction(CommonMovementActions.IS_STARTING_JUMP_BOOL,
                ()->inputListener.isKeyPressed(keyboardKeyMap.get(INPUT_JUMP)));
        this.registerAction(CommonMovementActions.ROTATION_DIRECTION_FLOAT, ()->-horizontalMovementSupplier());
        this.registerAction(CommonMovementActions.FORWARDS_BACKWARDS_FLOAT, ()->-verticalMovementSupplier());
    }

    /**
     * Assign a key to a given input function.
     * @param function A string representing the input function. If this function already had a key
     *                 mapped to it, it is replaced by the new key.
     * @param keyFromKeyEvent An int constant from the class {@link KeyEvent} to assign to the given function
     */
    public void setKeyboardKey(String function, int keyFromKeyEvent) {
        keyboardKeyMap.put(function, keyFromKeyEvent);
    }

    /**
     * Create a predicate of a given keyboard-key
     * @param keyfromKeyEvent one of the constants from {@link java.awt.event.KeyEvent}
     * @return a BooleanSupplier that returns true iff the given key is pressed
     */
    public BooleanSupplier inputPredicateOfKey(int keyfromKeyEvent) {
        return ()->inputListener.isKeyPressed(keyfromKeyEvent);
    }

    private Vector2 movementSupplier() {
        MutableVector2 movement =
                new MutableVector2(horizontalMovementSupplier(), verticalMovementSupplier());
        if(movement.x() != 0 && movement.y() != 0)
            movement.selfNormalize();
        return movement;
    }

    private float horizontalMovementSupplier() {
        float movement = 0;
        if(inputListener.isKeyPressed(keyboardKeyMap.get(INPUT_LEFT)))
            movement -= 1;
        if(inputListener.isKeyPressed(keyboardKeyMap.get(INPUT_RIGHT)))
            movement += 1;
        return movement;
    }

    private float verticalMovementSupplier() {
        float movement = 0;
        if(inputListener.isKeyPressed(keyboardKeyMap.get(INPUT_DOWN)))
            movement += 1;
        if(inputListener.isKeyPressed(keyboardKeyMap.get(INPUT_UP)))
            movement -= 1;
        return movement;
    }
}

package danogl.components.movement_schemes.movement_directing;

import danogl.gui.MessageHandler;
import danogl.gui.UserInputListener;
import danogl.gui.mouse.MouseButton;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * A MovementDirector that responds to mouse input.
 * An instance of this class is already constructed with limited builtin input functions.
 * The buttons for these can be altered,
 * and additional input functions (with their buttons) can be added.
 * @author Dan Nirel
 */
public class MouseMovementDirector extends MovementDirector {
    /**ID for the drag input function. While the button assigned to this input function is pressed,
     * a DragMovementScheme with this MovementDirector would drag the GameObject*/
    public static final String INPUT_DRAG               = "drag";
    /**ID for the advance to target input function. While the button assigned to this input function
     * is pressed, a TargetMovementScheme with this MovementDirector would move its GameObject towards the mouse position.*/
    public static final String INPUT_ADVANCE_TO_TARGET  = "advance";

    private final UserInputListener inputListener;
    private final Camera camera;

    private Map<String, MouseButton> mouseButtonMap = new HashMap<>(Map.of(
            INPUT_DRAG, MouseButton.LEFT_BUTTON,
            INPUT_ADVANCE_TO_TARGET, MouseButton.RIGHT_BUTTON
    ));

    /**
     * Returns a MovementStrategy that maps some mouse buttons to input functions.
     *
     * @param messages A MessageHandler to log error messages with
     * @param inputListener to read input through
     * @param camera the current camera used, in order to determine world-pos of the mouse position according
     *               to the camera. Can be null if no camera is used,
     *               if the camera maintains the default view, or if the returned
     *               MovementStrategy is meant for a MovementScheme of an object that is in
     *               camera-coordinates, i.e. moves with the camera.
     */
    public MouseMovementDirector(MessageHandler messages, UserInputListener inputListener,
                                 Camera camera) {
        super(messages);
        this.inputListener = inputListener;
        this.camera = camera;
        registerAction(CommonMovementActions.IS_DRAGGING_BOOL,
                () -> inputListener.isMouseButtonPressed(mouseButtonMap.get(INPUT_DRAG)));
        registerAction(CommonMovementActions.START_ADVANCING_TO_TARGET_BOOL,
                () -> inputListener.wasMouseButtonPressedThisFrame(mouseButtonMap.get(INPUT_ADVANCE_TO_TARGET)));
        registerAction(CommonMovementActions.STOP_ADVANCING_TO_TARGET_BOOL,
                () -> inputListener.wasMouseButtonReleasedThisFrame(mouseButtonMap.get(INPUT_ADVANCE_TO_TARGET)));

        Supplier<Vector2> mouseWorldPosSupplier =
                () -> camera == null ?
                        inputListener.getMouseScreenPos() :
                        camera.screenToWorldCoords(
                                inputListener.getMouseScreenPos()).getImmutableCopy();
        registerAction(CommonMovementActions.TARGET_VECTOR, mouseWorldPosSupplier);
        registerAction(CommonMovementActions.DRAG_POS_VECTOR, mouseWorldPosSupplier);
    }


    /**
     * Assign a key to a given input function.
     * @param function A string representing the input function. If this function already had a button
     *                 mapped to it, it is replaced by the new button.
     * @param button The mouse button to assign to the given function
     */
    public void setMouseButton(String function, MouseButton button) {
        mouseButtonMap.put(function, button);
    }

    /**
     * Creates a predicate of a given mouse button which returns true iff the button is pressed
     */
    public BooleanSupplier inputPredicateOfMouseButton(MouseButton button) {
        return () -> inputListener.isMouseButtonPressed(button);
    }
}
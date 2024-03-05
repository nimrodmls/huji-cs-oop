package danogl.components.movement_schemes.movement_directing;

/**
 * A collection of string-IDs for common movement instructions.
 * The various MovementSchemes ask their MovementDirector for a value
 * corresponding to some of these actions.
 * Each action contains in its suffix the type of value suitable for
 * that action (vector, float, or boolean).
 * The MovementScheme then uses that value to control the game object.
 * <br>For example, a PlatformerMovementScheme might ask its MovementDirector
 * for the current value corresponding to HORIZONTAL_MOVEMENT_FLOAT.
 * That value would then be used to control the GameObject's horizontal
 * velocity.
 * @author Dan Nirel
 */
public final class CommonMovementActions {
    public static final String
        MOVEMENT_VECTOR                 = "movement",
        HORIZONTAL_MOVEMENT_FLOAT       = "horizontal",
        VERTICAL_MOVEMENT_FLOAT         = "vertical",
        IS_STARTING_JUMP_BOOL           = "jump",
        ROTATION_DIRECTION_FLOAT        = "rotation",
        FORWARDS_BACKWARDS_FLOAT        = "forward",
        DRAG_POS_VECTOR                 = "drag_pos",
        IS_DRAGGING_BOOL                = "is_drag",
        TARGET_VECTOR                   = "target",
        START_ADVANCING_TO_TARGET_BOOL  = "start_advance_to_target",
        STOP_ADVANCING_TO_TARGET_BOOL   = "stop_advance_to_target";

    private CommonMovementActions() { }
}

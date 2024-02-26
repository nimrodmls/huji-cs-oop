package danogl.components.movement_schemes.movement_directing;

import danogl.gui.MessageHandler;
import danogl.gui.MsgLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A movement director answers queries regarding movements
 * an object should make. For example, it might be queried what horizontal
 * velocity the object should have. Possible implementations of a movement director
 * might be either based on user-input (in which case the arrow keys would determine
 * said velocity) or some AI-strategy.
 * <br>On the other side of a movement director, actions are registered with
 * a corresponding supplier. For example, the horizontal movement action might
 * be registered with a supplier that queries the user-input and returns
 * a suitable movement based on the pressed arrow keys.
 * <br>All in all, the movement director is a proxy, a de-coupler, between the actual agent
 * that decides on movements, to a MovementScheme that execute these movements.
 * @author Dan Nirel
 */
public class MovementDirector {
    private Map<String, Supplier<Object>> actionSuppliers = new HashMap<>();
    private MessageHandler messages;

    public MovementDirector(MessageHandler messages) {
        this.messages = messages;
    }

    /**
     * Register a movement with its corresponding supplier, which returns
     * a value for that movement.
     * @param actionID 
     * @param actionValueSupplier
     * @param <T>
     */
    public <T> void registerAction(String actionID, Supplier<T> actionValueSupplier) {
        actionSuppliers.put(actionID, (Supplier<Object>) actionValueSupplier);
    }

    /**
     * Get the value of the given action. For example, if the action represents horizontal movement,
     * the value would be a float signifying the amount of horizontal movement currently
     * employed (in this frame) by the MovementDirector.
     * @param actionID the actionID is agreed upon between the supplier of its value, i.e.
     *                 the MovementDirector, and its user, e.g. the MovementScheme.
     *                 The actionIDs used by DanoGameLab's MovementDirectors and MovementSchemes
     *                 are stored in {@link CommonMovementActions}.
     * @param defaultValue What value to return for the actionID if this action is not supported
     *                    by the MovementDirector. The method also uses the defaultValue to
     *                     infer the value's type (e.g. 0f would mean the value's type is a float,
     *                     Vector2.ZERO that the value's type is Vector2 and so on).
     * @return The action's value, in this frame, according to the MovementDirector. If the
     * action is not supported or returns a value with a different type than defaultValue,
     * the given defaultValue is returned.
     * @param <T> The type of the value of the action
     */
    public <T> T getActionValue(String actionID, T defaultValue) {
        var supplier = actionSuppliers.get(actionID);
        if(supplier == null)
            return defaultValue;
        try {
            return (T) supplier.get();
        } catch(ClassCastException e) {
            messages.showMessage(
                    String.format("The name '%s' exists as an action id, " +
                            "but it has a different" +
                            " type than that of the default value sent", actionID),
                    MsgLevel.ERROR);
            return defaultValue;
        }
    }
}

package pepse.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple event dispatcher - Allows for the creation of events
 * and the registration of listeners to those events.
 * @author Nimrod M.
 */
public class Dispatcher {

    private final HashMap<String, ArrayList<Runnable>> events;

    /**
     * Creates a new Dispatcher.
     */
    public Dispatcher() {
        events = new HashMap<>();
    }

    /**
     * Creates a new event - If the event already exists, nothing should happen.
     * @param name The name of the event to create.
     */
    public void createEvent(String name) {
        if (events.containsKey(name)) {
            return;
        }

        events.put(name, new ArrayList<>());
    }

    /**
     * Triggers an event - All the listeners of the event should be called.
     * @param eventName The name of the event to trigger.
     */
    public void setEvent(String eventName) {
        for (Runnable callback : events.get(eventName)) {
            callback.run();
        }
    }

    /**
     * Registers a listener to an event.
     * @param eventName The name of the event to register the listener to.
     * @param listener Listener's callback - called upon event trigger.
     */
    public void registerListener(String eventName, Runnable listener) {
        events.get(eventName).add(listener);
    }
}

package pepse.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Dispatcher {

    private final HashMap<String, ArrayList<Runnable>> events;

    public Dispatcher() {
        events = new HashMap<>();
    }

    public void createEvent(String name) {
        events.put(name, new ArrayList<>());
    }

    public void setEvent(String eventName) {
        for (Runnable callback : events.get(eventName)) {
            callback.run();
        }
    }

    public void registerListener(String eventName, Runnable listener) {
        events.get(eventName).add(listener);
    }
}

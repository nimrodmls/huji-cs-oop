package danogl;

import danogl.collisions.GameObjectCollection;
import danogl.gui.*;
import danogl.gui.rendering.Camera;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Experimental, for careful use. The API may change drastically
 * in future updates in a fashion that is not backwards-compatible.
 * It may also be removed or replaced.
 * <br>
 * A tools shelf, stored statically (a singleton),
 * in which to store tools to be used from anywhere in the program. A given type may be assigned
 * only one object at a time. When the tools shelf is asked for an object of that type, the currently stored object
 * of that type is returned.
 * <br>
 * This is meant as a somewhat better alternative to multiple singletons across the program (or simple
 * static storages), and acts as a hub for other singletons.
 * It decouples the client and the singleton: the singleton's client does not access it
 * directly, but rather asks the tools shelf for a singleton of the desired type. Depending on the context
 * the program may register different objects as the singleton for that type. These may either be different instances of the
 * required type or instances of subclasses. The program may also  change the registered singleton during runtime. For example,
 * a tester may reassign the singleton object before each test.
 * <br>
 * Another advantage over regular singletons is that different clients of the same singleton may be assigned different
 * object they would get when asking the tools shelf for the singleton (i.e. the singleton may be client-specific).
 * @author Dan Nirel
 * @deprecated experimental: do not use for production or in a long-term project.
 */
@Deprecated
public final class Tools {
    private static final Tools singleton = new Tools();

    private Camera camera;
    private UserInputListener inputListener;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private GameObjectCollection gameObjects;

    //map from a tool class to its stored instance
    private Map<Class, Object> tools;

    //map specific clients to "private" tool racks of their own
    private Map<Object, Tools> clientSpecificTools = null;

    /**The only way to use the class is to get this single instance of this class using this method*/
    public static Tools shelf() { return singleton; }

    /**
     * Register a singleton tool. This object would be returned as the singleton for its type, for all clients
     * for which no client-specific tool was registered using {@link #registerToolForClient(Object, Object)} or
     * {@link #registerToolForClientIfAbsent(Object, Object)}.
     * If an object was already registered for this type, the new tool replaces it.
     * @param tool the singleton assigned for this type. Can be null.
     * @return The previous object assigned for this role (or null)
     * @param <T> The type to which the tool is assigned
     */
    public <T> T registerTool(T tool) {
        Object oldTool = null;
        if(tool instanceof Camera) {
            oldTool = camera;
            camera = (Camera) tool;
        }
        else if(tool instanceof UserInputListener) {
            oldTool = inputListener;
            inputListener = (UserInputListener) tool;
        }
        else if(tool instanceof WindowController) {
            oldTool = windowController;
            windowController = (WindowController) tool;
        }
        else if(tool instanceof ImageReader) {
            oldTool = imageReader;
            imageReader = (ImageReader) tool;
        }
        else if(tool instanceof SoundReader) {
            oldTool = windowController;
            soundReader = (SoundReader) tool;
        }
        else if(tool instanceof GameObjectCollection) {
            oldTool = gameObjects;
            gameObjects = (GameObjectCollection) tool;
        }
        else {
            if(tools == null)
                tools = new HashMap<>();
            oldTool = tools.put(tool.getClass(), tool);
        }
        return (T) oldTool;
    }

    /**
     * Register a singleton tool. This object would be returned as the singleton for its type, for all clients
     * for which no client-specific tool was registered using {@link #registerToolForClient(Object, Object)} or
     * {@link #registerToolForClientIfAbsent(Object, Object)}.
     * However, if an object was already registered for this type (in a non-client-specific way), no change takes effect.
     * @param tool the singleton assigned for this type. Can be null, though that would make the
     *             method have no effect in any case.
     * @return true if the given tool was registered, false if another was already registered for the same type
     * @param <T> The type to which the tool is assigned
     */
    public <T> boolean registerToolIfAbsent(T tool) {
        T oldTool = registerTool(tool);
        if(oldTool != null) {
            registerTool(oldTool); //replace back
            return false;
        }
        return true;
    }

    /**
     * Register a singleton tool as the one that would be supplied for a specific client.
     * This object would be returned as the singleton for its type for the given client.
     * To make it also the returned object for other specific clients, call this method
     * for all of them. To make it the returned object for any client which was
     * not assigned a singleton specific to him, use {@link #registerTool(Object)} or
     * {@link #registerToolIfAbsent(Object)}.
     * <br>
     * If a tool has been registered using the non-client-specific methods,
     * this call takes precedence for the specific client.
     * <br>If a tool was already registered specifically for this client,
     * the new one replaces the old.
     * @param tool the singleton assigned for this type and for this client. Can be null. In that case,
     *             the client would receive null as the singleton for this type,
     *             regardless of any non-null tools other clients would receive.
     * @param client the client for which the tool is assigned as a singleton. If this parameter
     *               is null, the call has no effect and null is returned.
     * @return the previous tool that was registered SPECIFICALLY for this client,
     * or null if no tool was specific to this client. If no tool was specific to this client
     * but some non-null tool was assigned generally for this type (and thus this client
     * would have been returned a non-null tool in a previous call to {@link #getTool(Class, Object)}),
     * still null would be returned.
     * @param <T> The type to which the tool is assigned
     */
    public <T> T registerToolForClient(T tool, Object client) {
        if(client == null)
            return null;
        if(clientSpecificTools == null)
            clientSpecificTools = new HashMap<>();
        var clientToolRack = clientSpecificTools.computeIfAbsent(client, c->new Tools());
        return clientToolRack.registerTool(tool);
    }

    /**
     * Register a singleton tool as the one that would be supplied for a specific client.
     * This object would be returned as the singleton for its type for the given client.
     * To make it also the returned object for other specific clients, call this method
     * for all of them. To make it the returned object for any client which was
     * not assigned a singleton specific to him, use {@link #registerTool(Object)} or
     * {@link #registerToolIfAbsent(Object)}.
     * <br>
     * If a tool has been registered using the non-client-specific methods,
     * this call takes precedence for the specific client.
     * <br>If a tool was already registered specifically for this client,
     * the method has no effect. If a tool was registered, but in a non-client-specific manner,
     * this method would still assign the given tool to the client specifically.
     * @param tool the singleton assigned for this type and for this client. Can be null. In that case,
     *             and assuming no other tool was assigned specifically for this client,
     *             the client would receive null as the singleton for this type,
     *             regardless of any non-null tools other clients would receive.
     * @param client the client for which the tool is assigned as a singleton. If this parameter
     *               is null, the call has no effect and false is returned.
     * @return true if no tool was registered SPECIFICALLY for this client, or if null was
     * registered specifically, i.e. if the new tool was assigned to the client specifically.
     * False if a non-null tool was previously already assigned specifically to the client.
     * @param <T> The type to which the tool is assigned
     */
    public <T> boolean registerToolForClientIfAbsent(T tool, Object client) {
        if(client == null)
            return false;
        T oldTool = registerToolForClient(tool, client);
        if(oldTool != null) {
            registerTool(oldTool); //replace back
            return false;
        }
        return true;
    }

    /**
     * Get the tool assigned to the given class, for the given client.
     * This method is similar to {@link #getTool(Class, Object)}, with the difference being
     * what happens if no tool is found for the client. Instead of throwing an exception,
     * this method returns null.
     * @param toolClass the type of tool asked for
     * @param client the object asking for this tool. Usually, the calling object would send
     *               "this" as this parameter, if the tool is for its own use. Can be null,
     *               in which case the class would look only for a tool registered generally.
     * @return If a tool of this type was registered specifically for this client (including null),
     *      * that tool will be returned. Otherwise, if a tool of this class was registered generally
     *      * to no specific client, that tool will be returned. Otherwise, null is returned.
     * @param <T> The type of the required tool
     */
    public <T> T tryGetTool(Class<T> toolClass, Object client) {
        T requiredTool = null;
        //first try client specific tool
        if (clientSpecificTools != null) {
            var clientToolRack = clientSpecificTools.get(client);
            if (clientToolRack != null) {
                var clientTool = clientToolRack.getToolPrivate(tools.getClass());
                if (clientTool != null)
                    return (T) clientTool;
            }
        }
        //if doesn't exist, get general tool
        return (T) getToolPrivate(toolClass);
    }

    /**
     * Get the tool assigned to the given class, for the given client.
     * If a tool of this type was registered specifically for this client (including null),
     * that tool will be returned. Otherwise, if a tool of this class was registered generally
     * to no specific client, that tool will be returned.
     * If none of these hold, a NoSuchElementException is thrown.
     * @param toolClass the type of tool asked for
     * @param client the object asking for this tool. Usually, the calling object would send
     *               "this" as this parameter, if the tool is for its own use. Can be null,
     *               in which case the class would look only for a tool registered generally.
     * @return If a tool of this type was registered specifically for this client (including null),
     *      * that tool will be returned. Otherwise, if a tool of this class was registered generally
     *      * to no specific client, that tool will be returned.
     * @throws NoSuchElementException if no tool is available to return for this client
     * @param <T> The type of the required tool
     */
    public <T> T getTool(Class<T> toolClass, Object client) {
        T requiredTool = tryGetTool(toolClass, client);

        //if requiredTool is null, then it was never registered -
        //show an error message
        if(requiredTool == null) {
            String msg = "A tool of type "+
                    toolClass.getName()+
                    " was not registered to the Tools.shelf";
            if(windowController != null) {
                //then able to show an error message
                windowController.messages().showMessage(msg, MsgLevel.CRITICAL);
            }
            //default critical error msg should already have exited the app
            //but in case it was altered, or if a WindowController wasn't registered:
            throw new NoSuchElementException(msg);
        }
        return requiredTool;
    }

    //block the creation of other instances of this class
    private Tools() { }

    private Object getToolPrivate(Class toolClass) {
        if(toolClass.equals(Camera.class))
            return camera;
        if(toolClass.equals(UserInputListener.class))
            return inputListener;
        if(toolClass.equals(WindowController.class))
            return windowController;
        if(toolClass.equals(ImageReader.class))
            return imageReader;
        if(toolClass.equals(SoundReader.class))
            return soundReader;
        if(toolClass.equals(GameObjectCollection.class))
            return gameObjects;
        return tools.get(toolClass);
    }
}

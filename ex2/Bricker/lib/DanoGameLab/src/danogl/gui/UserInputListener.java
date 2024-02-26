package danogl.gui;

import danogl.components.movement_schemes.movement_directing.KeyboardMovementDirector;
import danogl.components.movement_schemes.movement_directing.MouseMovementDirector;
import danogl.gui.mouse.MouseButton;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

import java.util.Set;

/**
 * An interface for reading user input in the current frame
 */
public interface UserInputListener {
    /**
     * Returns whether a key is currently down. This method will return true
     * for a given key as long as it is pressed (i.e. from the frame in which
     * the key was held down until the frame in which the key was released).
     * @param keyFromKeyEvent a key code from the class KeyEvent (KeyEvent.VK_...)
     * @return is the specified key held down in the current frame
     */
    boolean isKeyPressed(int keyFromKeyEvent);

    /**
     * Returns whether a given key was just pressed. This method returns true
     * for a key only in the frame in which it was depressed.
     * @param keyFromKeyEvent a key code from the class KeyEvent (KeyEvent.VK_...)
     * @return true iff the key was pressed down in this frame
     */
    boolean wasKeyPressedThisFrame(int keyFromKeyEvent);

    /**
     * Returns whether a given key was just released. This method returns true
     * for a key only in the frame in which it was released.
     * @param keyFromKeyEvent a key code from the class KeyEvent (KeyEvent.VK_...)
     * @return was the specified key released in this frame
     */
    boolean wasKeyReleasedThisFrame(int keyFromKeyEvent);

    /**
     * Returns whether a given mouse button was just clicked (pressed and released without moving).
     * This method returns true for a button only in the frame in which the click ended. The
     * method returns true in addition to {@link #wasMouseButtonPressedThisFrame(MouseButton)} and
     * {@link #wasMouseButtonReleasedThisFrame(MouseButton)}.
     * @param button the queried mouse button
     * @return was the specified button released in this frame
     */
    boolean wasMouseButtonClickedThisFrame(MouseButton button);

    /**
     * Returns whether a given mouse button was just depressed - either
     * as a part of "mouse click" or not.
     * This method returns true for a button only in the frame in which it was depressed.
     * @param button the queried mouse button
     * @return true iff the mouse button was pressed down in this frame
     */
    boolean wasMouseButtonPressedThisFrame(MouseButton button);

    /**
     * Returns whether a given mouse button was just released. This method returns true
     * for a button only in the frame in which it was released. Also called at the end of a click.
     * @param button the queried mouse button
     * @return true iff the specified mouse button was released in this frame
     */
    boolean wasMouseButtonReleasedThisFrame(MouseButton button);

    /**
     * Returns whether a mouse button is currently down/depressed/pressed.
     * @param button the queried mouse button
     * @return is the specified button down in this frame
     */
    boolean isMouseButtonPressed(MouseButton button);

    /**
     * Returns the mouse's coordinates in the standard danogl coordinate system,
     * i.e. in pixels, where the top-left pixel is (0,0). This is relative to the screen,
     * i.e., window. If a camera is used and you need the mouse's position in world coordinates,
     * use the camera's transformation method screenToWorldCoords.
     */
    Vector2 getMouseScreenPos();

    /**
     * Returns the number of clicks the mousewheel was rotated this frame.
     * @return negative values for scrolling up, positive for scrolling down.
     */
    double mouseWheelClicksThisFrame();

    /**
     * The set of keys that is currently depressed/down/pressed.
     * @return a set of integers that are constants in KeyEvent.
     */
    Set<Integer> pressedKeys();

    /**
     * The set of mouse-buttons that are currently pressed.
     * @return a set of mouse-button from MouseButton.
     */
    Set<MouseButton> pressedMouseButtons();

    /**
     * A movement director based on mouse-input, to be used by classes
     * that would perform movement based on an external movement director,
     * such as MovementSchemes in @link{danogl.components.controllers}
     * @param camera the camera is used to determine world coordinates
     *               based on cursor position. Can be null if no camera is used.
     */
    MouseMovementDirector mouseMovementDirector(Camera camera);

    /**
     * A movement director based on keyboard-input, to be used by classes
     * that would perform movement based on an external movement director,
     * such as MovementSchemes in @link{danogl.components.controllers}
     */
    KeyboardMovementDirector keyboardMovementDirector();
}

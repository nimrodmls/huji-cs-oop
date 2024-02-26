package danogl.gui;

import danogl.components.movement_schemes.movement_directing.KeyboardMovementDirector;
import danogl.components.movement_schemes.movement_directing.MouseMovementDirector;
import danogl.gui.mouse.MouseButton;
import danogl.gui.rendering.Camera;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * A package private class
 * @author Dan Nirel
 */
class KeyboardMouseUserInputListener extends KeyAdapter implements
        UserInputListener, MouseListener, MouseWheelListener {
    private Set<Integer> pressedKeys = new HashSet<>();
    private Set<Integer> keysDownThisFrame = new HashSet<>();
    private Set<Integer> keysUpThisFrame = new HashSet<>();
    private EnumSet<MouseButton> pressedMouseButtons = EnumSet.noneOf(MouseButton.class);
    private EnumSet<MouseButton> clickedMouseButtons = EnumSet.noneOf(MouseButton.class);
    private EnumSet<MouseButton> mouseButtonsDownThisFrame = EnumSet.noneOf(MouseButton.class);
    private EnumSet<MouseButton> mouseButtonsUpThisFrame = EnumSet.noneOf(MouseButton.class);
    private MutableVector2 mousePos = new MutableVector2();
    private double mouseWheelClicksThisFrame;
    private MessageHandler messages;

    public KeyboardMouseUserInputListener(MessageHandler messages) {
        this.messages = messages;
    }

    public void update(float deltaTime) {
        keysDownThisFrame.clear();
        keysUpThisFrame.clear();
        mouseButtonsDownThisFrame.clear();
        mouseButtonsUpThisFrame.clear();
        clickedMouseButtons.clear();
        mouseWheelClicksThisFrame = 0;
    }

    @Override
    public boolean isKeyPressed(int keyFromKeyEvent) {
        return pressedKeys.contains(keyFromKeyEvent);
    }

    @Override
    public boolean wasKeyPressedThisFrame(int keyFromKeyEvent) {
        return keysDownThisFrame.contains(keyFromKeyEvent);
    }

    @Override
    public boolean wasKeyReleasedThisFrame(int keyFromKeyEvent) {
        return keysUpThisFrame.contains(keyFromKeyEvent);
    }

    @Override
    public boolean wasMouseButtonClickedThisFrame(MouseButton button) {
        return clickedMouseButtons.contains(button);
    }

    @Override
    public boolean wasMouseButtonPressedThisFrame(MouseButton button) {
        return mouseButtonsDownThisFrame.contains(button);
    }

    @Override
    public boolean wasMouseButtonReleasedThisFrame(MouseButton button) {
        return mouseButtonsUpThisFrame.contains(button);
    }

    @Override
    public boolean isMouseButtonPressed(MouseButton button) {
        return pressedMouseButtons.contains(button);
    }

    @Override
    public Vector2 getMouseScreenPos() {
        var mousePoint = MouseInfo.getPointerInfo().getLocation();
        mousePos.setXY(mousePoint.x, mousePoint.y);
        return mousePos;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(pressedKeys.contains(e.getKeyCode()))
            return;
        keysDownThisFrame.add(e.getKeyCode());
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        keysUpThisFrame.add(e.getKeyCode());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        var button = MouseButton.mouseIntToMouseButton(e.getButton());
        if(button != null)
            clickedMouseButtons.add(button);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        var button = MouseButton.mouseIntToMouseButton(e.getButton());
        if(button != null) {
            pressedMouseButtons.add(button);
            mouseButtonsDownThisFrame.add(button);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        var button = MouseButton.mouseIntToMouseButton(e.getButton());
        if(button != null) {
            pressedMouseButtons.remove(button);
            mouseButtonsUpThisFrame.add(button);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseWheelClicksThisFrame = e.getPreciseWheelRotation();
    }
    @Override
    public double mouseWheelClicksThisFrame() {
        return mouseWheelClicksThisFrame;
    }
    @Override
    public Set<Integer> pressedKeys() {
        return pressedKeys;
    }
    @Override
    public Set<MouseButton> pressedMouseButtons() {
        return pressedMouseButtons;
    }

    @Override
    public MouseMovementDirector mouseMovementDirector(Camera camera) {
        return new MouseMovementDirector(messages, this, camera);
    }

    @Override
    public KeyboardMovementDirector keyboardMovementDirector() {
        return new KeyboardMovementDirector(messages, this);
    }
}

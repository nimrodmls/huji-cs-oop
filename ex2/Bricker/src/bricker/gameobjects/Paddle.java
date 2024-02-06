package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300.0f;
    private static final float BORDER_FIX_THRESHOLD = 10.0f;
    private final Vector2 boardLength;
    private final UserInputListener inputListener;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener Receiving the input from the user, to control the Paddle.
     */
    public Paddle(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  Vector2 windowDimensions,
                  Renderable renderable,
                  UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable);
        this.boardLength = windowDimensions;
        this.inputListener = inputListener;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 movementDirection = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDirection = movementDirection.add(Vector2.LEFT);
        } else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDirection = movementDirection.add(Vector2.RIGHT);
        }

        Vector2 currentPos = getTopLeftCorner();
        if (currentPos.x() < 0) {
            setTopLeftCorner(currentPos.add(new Vector2(BORDER_FIX_THRESHOLD, 0)));
            movementDirection = Vector2.ZERO;
        } else if (boardLength.x() < currentPos.x() + getDimensions().x()) {
            setTopLeftCorner(currentPos.subtract(new Vector2(BORDER_FIX_THRESHOLD, 0)));
            movementDirection = Vector2.ZERO;
        }

        setVelocity(movementDirection.mult(MOVEMENT_SPEED));
    }
}

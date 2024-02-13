package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;
import bricker.utilities.GameConstants;
import bricker.utilities.Utils;

/**
 * The PuckStrategy creates on collision with the relevant brick,
 * a specified set of puck balls to roam freely alongside the player's ball.
 * @author Nimrod M.
 */
public class PuckStrategy extends BasicCollisionStrategy {

    // The puck balls are 75% the size of the primary ball
    private final Vector2 PUCK_BALLS_DIMENSIONS = GameConstants.PRIMARY_BALL_DIMENSIONS.mult(0.75f);

    private final BrickerGameManager gameManager;
    private final int ballCount;
    private final Sound hitSound;
    private final ImageRenderable ballImage;

    /**
     * Constructs a PuckStrategy object with the specified parameters.
     *
     * @param gameManager   The game manager responsible for managing the game state.
     * @param brickGrid     The brick grid representing the game board.
     * @param ballCount     The number of puck balls to be created when a collision occurs.
     * @param hitSound      The sound to be played when a collision occurs.
     * @param ballImage     The image to be used for the puck balls.
     */
    public PuckStrategy(
        BrickerGameManager gameManager, 
        BrickGrid brickGrid, 
        int ballCount, 
        Sound hitSound, 
        ImageRenderable ballImage) {
            
        super(brickGrid);
        this.gameManager = gameManager;
        this.ballCount = ballCount;
        this.hitSound = hitSound;
        this.ballImage = ballImage;
    }

    /**
     * Handles the collision with a puck brick.
     * This will create the puck balls.
     *
     * @param object1 The first game object involved in the collision.
     * @param object2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        // Creating the balls, as per the ball count requested
        for (int currentBall = 0; currentBall < ballCount; currentBall++) {

            Ball ball = new Ball(
                    object1.getCenter(),
                    PUCK_BALLS_DIMENSIONS,
                    ballImage,
                    hitSound);
            gameManager.addGameObject(ball, Layer.DEFAULT);
            Utils.randomizeAltBallVelocity(ball, GameConstants.PRIMARY_BALL_SPEED);
        }
    }
}

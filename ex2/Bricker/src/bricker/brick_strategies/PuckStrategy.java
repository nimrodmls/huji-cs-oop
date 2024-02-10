package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;
import bricker.utilities.GameConstants;
import bricker.utilities.Utils;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class PuckStrategy extends BasicCollisionStrategy {

    // The puck balls are 75% the size of the primary ball
    private final Vector2 PUCK_BALLS_DIMENSIONS = GameConstants.PRIMARY_BALL_DIMENSIONS.mult(0.75f);

    private final BrickerGameManager gameManager;
    private final int ballCount;
    private final SoundReader soundReader;
    private final ImageReader imageReader;

    public PuckStrategy(BrickerGameManager gameManager, BrickGrid brickGrid, int ballCount, SoundReader soundReader, ImageReader imageReader) {
        super(gameManager, brickGrid);
        this.gameManager = gameManager;
        this.ballCount = ballCount;
        this.soundReader = soundReader;
        this.imageReader = imageReader;
    }

    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        // Loading needed assets only once!
        Renderable ballImage =
                imageReader.readImage("asserts/mockBall.png", true);
        Sound collisionSound = soundReader.readSound(
                "asserts/blop_cut_silenced.wav");

        // Creating the balls, as per the ball count requested
        for (int currentBall = 0; currentBall < ballCount; currentBall++) {

            Ball ball = new Ball(
                    object1.getCenter(),
                    PUCK_BALLS_DIMENSIONS,
                    ballImage,
                    collisionSound);
            gameManager.addGameObject(ball, Layer.DEFAULT);
            Utils.randomizeBallVelocity(ball, GameConstants.PRIMARY_BALL_SPEED);
        }
    }
}

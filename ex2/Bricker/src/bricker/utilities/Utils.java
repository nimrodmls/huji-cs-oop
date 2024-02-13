package bricker.utilities;

import bricker.gameobjects.Ball;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A collection of utility methods for the game.
 */
public class Utils {

    /**
     * Randomizes the velocity vector of the ball, with a constant speed.
     * @param ball          The ball to randomize its velocity vector.
     * @param BALL_SPEED    The speed of the ball.
     */
    public static void randomizeBallVelocity(Ball ball, float BALL_SPEED) {
        // Initial velocity vector is selected randomly, to constant speed
        float ballSpeedX = BALL_SPEED;
        float ballSpeedY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballSpeedX *= -1;
        } else {
            ballSpeedY *= -1;
        }

        // Setting starting velocity vector & the starting position of the ball
        ball.setVelocity(new Vector2(ballSpeedX, ballSpeedY));
    }

    /**
     * Randomizes the velocity vector of the ball, with a constant speed.
     * This is an alternative method to the original.
     *
     * @param ball         The ball to randomize its velocity vector.
     * @param BALL_SPEED   The speed of the ball.
     */
    public static void randomizeAltBallVelocity(Ball ball, float BALL_SPEED) {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float)Math.cos(angle) * BALL_SPEED;
        float velocityY = (float)Math.sin(angle) * BALL_SPEED;

        ball.setVelocity(new Vector2(velocityX, velocityY));
    }
}

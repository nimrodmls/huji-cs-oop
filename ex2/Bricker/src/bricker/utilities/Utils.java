package bricker.utilities;

import bricker.gameobjects.Ball;
import danogl.util.Vector2;

import java.util.Random;

public class Utils {
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
}

package bricker.utilities;

import danogl.util.Vector2;

public class GameConstants {

    // Asset pathing constants
    public static final String ASSET_DIR_PATH = "assets/";
    public static final String HEART_ASSET_PATH = ASSET_DIR_PATH + "heart.png";
    public static final String BRICK_ASSET_PATH = ASSET_DIR_PATH + "brick.png";
    public static final String BALL_ASSET_PATH = ASSET_DIR_PATH + "ball.png";
    public static final String PADDLE_ASSET_PATH = ASSET_DIR_PATH + "paddle.png";
    public static final String BACKGROUND_ASSET_PATH = ASSET_DIR_PATH + "DARK_BG2_small.jpeg";
    public static final String COLLISION_SOUND_PATH = ASSET_DIR_PATH + "blop_cut_silenced.wav";
    public static final String PUCK_BALL_ASSET_PATH = ASSET_DIR_PATH + "mockBall.png";

    // Ball constants
    public static float PRIMARY_BALL_SPEED = 200.0f;
    public static final Vector2 PRIMARY_BALL_DIMENSIONS = new Vector2(20, 20);

    // Paddle constants
    public static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);

    // Strategy constants
    public static final int PUCK_BALL_COUNT = 2;
}

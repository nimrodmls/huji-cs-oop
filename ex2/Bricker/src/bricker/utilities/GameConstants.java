package bricker.utilities;

import danogl.util.Vector2;

/**
 * Constants used in the game.
 * These are not esoteric constants for each class, but rather constants
 * that alter the game's behavior and are used by multiple classes.
 */
public class GameConstants {

    /**
     * UI constants
     */
    public static final Vector2 UI_GRID_ELEMENT_DIMENSIONS = new Vector2(20, 20);

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

    /**
     * Paddle constants
     */
    public static final float PADDLE_MOVEMENT_SPEED = 300.0f;
    public static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);

    /**
     * Strategy constants
     */
    public static final int PUCK_BALL_COUNT = 2;
    // Maximum number of nested double actions allowed
    public static final int ALLOWED_DOUBLE_ACTIONS = 2;
    // Setting the falling heart dimensions to be the same as they are in the UI
    public static final Vector2 FALLING_HEART_DIMENSIONS = UI_GRID_ELEMENT_DIMENSIONS;
}

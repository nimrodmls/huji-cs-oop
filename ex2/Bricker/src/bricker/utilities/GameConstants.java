package bricker.utilities;

import danogl.util.Vector2;

/**
 * Constants used in the game.
 * These are not esoteric constants for each class, but rather constants
 * that alter the game's behavior and are used by multiple classes.
 * @author Nimrod M.
 */
public class GameConstants {

    /**
     * Private constructor to prevent instantiation. This class is a utility class.
     */
    private GameConstants() {}

    /**
     * The title of the game
     */
    public static final String GAME_TITLE = "Bricker";
    /**
     * The size of the window
     */
    public static final Vector2 WINDOW_SIZE = new Vector2(700, 500);
    /**
     * The dimensions of the game board, in pixels
     */
    public static final Vector2 UI_GRID_DIMENSIONS_PIXELS = new Vector2(20, WINDOW_SIZE.y() - 60);
    /**
     * The dimensions of the game board, in elements
     */
    public static final Vector2 UI_GRID_DIMENSIONS_ELEMENTS = new Vector2(4    , 2);
    /**
     * The dimensions of each element in the game board, in pixels
     */
    public static final Vector2 UI_GRID_ELEMENT_DIMENSIONS = new Vector2(20, 20);
    /**
     * The prompt to display when the player wins
     */
    public static final String WIN_PROMPT = "You win! Play again?";
    /**
     * The prompt to display when the player loses
     */
    public static final String LOSE_PROMPT = "You lose! Play again?";

    /**
     * Asset constants
     */
    /**
     * The root path to the assets directory
     */
    public static final String ASSET_DIR_PATH = "assets/";
    /**
     * The path to the heart image asset
     */
    public static final String HEART_ASSET_PATH = ASSET_DIR_PATH + "heart.png";
    /**
     * The path to the brick image asset
     */
    public static final String BRICK_ASSET_PATH = ASSET_DIR_PATH + "brick.png";
    /**
     * The path to the ball image asset
     */
    public static final String BALL_ASSET_PATH = ASSET_DIR_PATH + "ball.png";
    /**
     * The path to the paddle image asset
     */
    public static final String PADDLE_ASSET_PATH = ASSET_DIR_PATH + "paddle.png";
    /**
     * The path to the background image asset
     */
    public static final String BACKGROUND_ASSET_PATH = ASSET_DIR_PATH + "DARK_BG2_small.jpeg";
    /**
     * The path to the collision sound asset
     */
    public static final String COLLISION_SOUND_PATH = ASSET_DIR_PATH + "blop_cut_silenced.wav";
    /**
     * The path to the puck ball asset
     */
    public static final String PUCK_BALL_ASSET_PATH = ASSET_DIR_PATH + "mockBall.png";

    /**
     * The initial lives a player has
     */
    public static final int INITIAL_LIVES = 3;
    /**
     * The maximum number of lives a player can have
     */
    public static final int MAX_LIFE_COUNT = 4;

    /**
     * Speed of the primary ball
     */
    public static float PRIMARY_BALL_SPEED = 200.0f;
    /**
     * The dimensions of the primary ball of the game board, in pixels
     */
    public static final Vector2 PRIMARY_BALL_DIMENSIONS = new Vector2(20, 20);

    /**
     * The speed of the paddle
     */
    public static final float PADDLE_MOVEMENT_SPEED = 300.0f;
    /**
     * The dimensions of the paddle, in pixels
     */
    public static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);

    /**
     * The number of puck balls spawned when the puck brick is hit
     */
    public static final int PUCK_BALL_COUNT = 2;
    /**
     * The maximum amount of nested double actions
     */
    public static final int ALLOWED_DOUBLE_ACTIONS = 2;
    /**
     * Setting the falling heart dimensions to be the same as they are in the UI
     */
    public static final Vector2 FALLING_HEART_DIMENSIONS = UI_GRID_ELEMENT_DIMENSIONS;
}

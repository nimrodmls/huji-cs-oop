package bricker.main;

import java.awt.event.KeyEvent;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import bricker.factory.StrategyFactory;
import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.Paddle;
import bricker.gameobjects.UserInterface;
import bricker.utilities.GameConstants;
import bricker.utilities.Utils;

public class BrickerGameManager extends GameManager {

    private static final int BRICK_HEIGHT_PIXELS = 15;
    private static final int DEFAULT_BRICK_COUNT_PER_ROW = 8;
    private static final int DEFAULT_BRICK_ROW_COUNT = 7;
    private static final float WALL_WIDTH_PIXELS = 10.0f;
    // Where in the window we start placing bricks
    private static final Vector2 BRICK_BASE_POSITION =
            new Vector2(WALL_WIDTH_PIXELS*2, WALL_WIDTH_PIXELS*2);

    private final int brickCountPerRow;
    private final int brickRowCount;
    private Ball ball;
    private BrickGrid brickGrid;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private int lifeCounter;
    private UserInterface userInterface;
    private UserInputListener userInputListener;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Paddle userPaddle;

    /**
     * Construct a new Bricker Game Manager.
     * This object is responsible for the game's logic and state.
     *
     * @param title            The title of the game window.
     * @param windowSize       The size of the game window.
     * @param brickCountPerRow The number of bricks in a row.
     * @param brickRowCount    The number of rows of bricks.
     */
    public BrickerGameManager(String title, Vector2 windowSize, int brickCountPerRow, int brickRowCount) {
        super(title, windowSize);
        this.brickCountPerRow = brickCountPerRow;
        this.brickRowCount = brickRowCount;
    }

    /**
     * Starting a fresh run of the game.
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader,
            UserInputListener inputListener,
            WindowController windowController) {

        super.initializeGame(
                imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.userInputListener = inputListener;
        windowDimensions = windowController.getWindowDimensions();

        // Collision rules in the foreground should not exist (interaction between bricks etc.)
        gameObjects().layers().shouldLayersCollide(
                Layer.FOREGROUND, Layer.FOREGROUND, false);

        gameObjects().layers().shouldLayersCollide(
                Layer.DEFAULT, Layer.FOREGROUND, true);

        // Setting the initial game state
        lifeCounter = GameConstants.INITIAL_LIVES;

        // Instantiating all the game objects
        createPrimaryBall();
        createUserPaddle();
        createBoardWalls();
        addBackground();
        createBricks();
        initiateUI();
    }

    /**
     * Validates the state of the game and the winning/losing conditions.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        endgameHandler();
    }

    /**
     * Adding a GameObject to the game.
     *
     * @param object        The GameObject to add.
     * @param objectLayer   The layer to which to add the GameObject.
     */
    public void addGameObject(GameObject object, int objectLayer) {
        gameObjects().addGameObject(object, objectLayer);
    }

    /**
     * Removing a GameObject from the game.
     *
     * @param object      The GameObject to remove.
     * @param objectLayer The layer from which to remove the GameObject.
     * @return            True if the object was removed, false otherwise.
     */
    public boolean removeGameObject(GameObject object, int objectLayer) {
        return gameObjects().removeGameObject(object, objectLayer);
    }

    /**
     * Adding a life to the player's life count.
     * If the player has reached the maximum amount of lives, the life will not be added.
     * The function cannot fail.
     */
    public void addLife() {
        if (lifeCounter < GameConstants.MAX_LIFE_COUNT) {
            userInterface.addHeart(imageReader);
            lifeCounter++;
        }
    }

    /**
     * Removing a life from the player's life count.
     *
     * @return True if the player has no more lives, false otherwise.
     */
    public boolean removeLife() {
        if (lifeCounter > 0) {
            userInterface.removeHeart();
            lifeCounter--;
        }

        return 0 == lifeCounter;
    }

    /**
     * Resetting the position of the ball and its velocity to default.
     *
     * @param ball              The ball to reset.
     * @param windowDimensions  The dimensions of the window.
     */
    private static void restartBall(Ball ball, Vector2 windowDimensions) {
        Utils.randomizeBallVelocity(ball, GameConstants.PRIMARY_BALL_SPEED);
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    /**
     * Creating the user's paddle.
     */
    private void createUserPaddle() {
        Renderable paddleImage = imageReader.readImage(
                GameConstants.PADDLE_ASSET_PATH, true);

        userPaddle = new Paddle(
                        Vector2.ZERO,
                        GameConstants.PADDLE_DIMENSIONS,
                        windowDimensions,
                        paddleImage,
                        userInputListener);
        userPaddle.setCenter(
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 40));

        this.gameObjects().addGameObject(userPaddle, Layer.FOREGROUND);
    }

    /**
     * Creating the primary play ball of the game.
     */
    private void createPrimaryBall() {
        Renderable ballImage =
                imageReader.readImage(GameConstants.BALL_ASSET_PATH, true);
        Sound collisionSound = soundReader.readSound(
                GameConstants.COLLISION_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, GameConstants.PRIMARY_BALL_DIMENSIONS, ballImage, collisionSound);
        restartBall(ball, windowDimensions);
        this.gameObjects().addGameObject(ball);
    }

    /**
     * Adding the background to the game.
     */
    private void addBackground() {
        Renderable background = imageReader.readImage(
                GameConstants.BACKGROUND_ASSET_PATH, false);
        GameObject backgroundObject = new GameObject(
                Vector2.ZERO, windowDimensions, background);
        backgroundObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    /**
     * Initiating the UI of the game.
     */
    private void initiateUI() {
        userInterface = new UserInterface(
                GameConstants.UI_GRID_DIMENSIONS_PIXELS,
                GameConstants.UI_GRID_DIMENSIONS_ELEMENTS,
                GameConstants.UI_GRID_ELEMENT_DIMENSIONS,
                this,
                GameConstants.MAX_LIFE_COUNT);
        // Initializing the hearts for the start of the game
        for (int iter = 0; iter < lifeCounter; iter++) {
            userInterface.addHeart(imageReader);
        }
    }

    /**
     * Creating the walls that surround the game board.
     * The walls are not rendered, but they are used for collision detection.
     * Upon collision with the walls, the object will receive a notification via its proper method.
     */
    private void createBoardWalls() {
        Vector2 horizontalWall = new Vector2(windowDimensions.x(), WALL_WIDTH_PIXELS);
        Vector2 verticalWall = new Vector2(WALL_WIDTH_PIXELS, windowDimensions.y());
        // Defining Position-Wall Pairs
        Vector2[][] wallPositions = new Vector2[][] {
                { Vector2.ZERO, horizontalWall }, // Top Wall
                { Vector2.ZERO, verticalWall }, // Left Wall
                { new Vector2(windowDimensions.x() - WALL_WIDTH_PIXELS, 0), verticalWall } // Right Wall
        };

        for (Vector2[] positionPair : wallPositions) {
            GameObject wallObject = new GameObject(
                    positionPair[0], positionPair[1], null);
            this.gameObjects().addGameObject(wallObject, Layer.FOREGROUND);
        }
    }

    /**
     * Creating the bricks for the game.
     * The bricks are placed in a grid, and the behavior of each brick is determined by
     * a strategy generated randomly.
     */
    private void createBricks() {
        Renderable brickImage = imageReader.readImage(
                GameConstants.BRICK_ASSET_PATH, false);

        // Calculating the amount of pixels that the bricks can take up on the screen
        // this does not include the walls, since the player cannot reach
        float maxRowLength = windowDimensions.x() - (BRICK_BASE_POSITION.x() * 2);

        brickGrid = new BrickGrid(
                BRICK_BASE_POSITION,
                new Vector2(brickCountPerRow, brickRowCount),
                BRICK_HEIGHT_PIXELS,
                maxRowLength,
                this);
        gameObjects().addGameObject(brickGrid, Layer.BACKGROUND);

        StrategyFactory strategyFactory = new StrategyFactory(
                this,
                brickGrid,
                imageReader,
                soundReader,
                userInputListener,
                windowDimensions,
                ball,
                userPaddle
        );

        // Filling the brick grid
        for (int row = 0; row < brickRowCount; row++) {
            for (int col = 0; col < brickCountPerRow; col++) {
                brickGrid.addObject(col, row, brickImage, strategyFactory.createRandomStrategy());
            }
        }
    }

    /**
     * Handling the endgame scenarios.
     */
    private void endgameHandler() {
        String prompt;
        // Losing scenario - Ball left the window (from below)
        if (ball.getCenter().y() > windowDimensions.y()) {

            if (!removeLife()) {
                restartBall(ball, windowDimensions);
                return;
            }

            prompt = GameConstants.LOSE_PROMPT;
        }
        // Winning scenario - No bricks remain or user pressed 'W' button
        else if (0 == brickGrid.getBrickCount() || userInputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = GameConstants.WIN_PROMPT;
        // No win or lose - the game should continue normally
        } else {
            return;
        }

        // If we reached here then the game has ended, we should ask the user whether to reset
        if (windowController.openYesNoDialog(prompt)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    /**
     * Running the game.
     * @param args The command line arguments. Should receive two integers, the first
     *             representing the amount of bricks per row, and the second representing
     *             the amount of rows of bricks.
     */
    public static void main(String[] args) {
        BrickerGameManager gameManager;

        if (2 > args.length) {
            gameManager = new BrickerGameManager(
                    GameConstants.GAME_TITLE,
                    GameConstants.WINDOW_SIZE,
                    DEFAULT_BRICK_COUNT_PER_ROW,
                    DEFAULT_BRICK_ROW_COUNT);
        }
        else if (2 == args.length) {
            gameManager = new BrickerGameManager(
                    GameConstants.GAME_TITLE,
                    GameConstants.WINDOW_SIZE,
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]));
        }
        else {
            System.out.println("Usage: Bricker [brickCountPerRow] [brickRowCount]");
            return;
        }

        gameManager.run();
    }
}

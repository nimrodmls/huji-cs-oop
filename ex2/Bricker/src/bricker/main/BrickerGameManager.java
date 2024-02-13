package bricker.main;

import bricker.factory.StrategyFactory;
import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.Paddle;
import bricker.gameobjects.UserInterface;
import bricker.utilities.GameConstants;
import bricker.utilities.Utils;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class BrickerGameManager extends GameManager {

    private static final Vector2 UI_GRID_ELEMENT_DIMENSIONS = new Vector2(20, 20);
    private static final int DEFAULT_GAME_COUNT = 3;
    private static final int BRICK_HEIGHT_PIXELS = 15;
    private static final int DEFAULT_BRICK_COUNT_PER_ROW = 8;
    private static final int DEFAULT_BRICK_ROW_COUNT = 7;
    private static final float WALL_WIDTH_PIXELS = 10.0f;
    // Where in the window we start placing bricks
    private static final Vector2 BRICK_BASE_POSITION =
            new Vector2(WALL_WIDTH_PIXELS*2, WALL_WIDTH_PIXELS*2);
    private static final int MAX_LIFE_COUNT = 4;

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
     * Construct a new GameManager instance.
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

    public void addGameObject(GameObject object, int objectLayer) {
        gameObjects().addGameObject(object, objectLayer);
    }

    public boolean removeGameObject(GameObject object, int objectLayer) {
        return gameObjects().removeGameObject(object, objectLayer);
    }

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


        // Creating the Ball
        Renderable ballImage =
                imageReader.readImage(GameConstants.BALL_ASSET_PATH, true);
        Sound collisionSound = soundReader.readSound(
                GameConstants.COLLISION_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, GameConstants.PRIMARY_BALL_DIMENSIONS, ballImage, collisionSound);
        restartBall(ball, windowDimensions);
        this.gameObjects().addGameObject(ball);

        Renderable paddleImage = imageReader.readImage(
                GameConstants.PADDLE_ASSET_PATH, true);

        // Create User Paddle
        userPaddle =
                new Paddle(
                        Vector2.ZERO,
                        GameConstants.PADDLE_DIMENSIONS,
                        windowDimensions,
                        paddleImage,
                        inputListener);
        userPaddle.setCenter(
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 40));

        this.gameObjects().addGameObject(userPaddle, Layer.FOREGROUND);

        // Instantiating the rest of the objects
        createBoardWalls();
        addBackground(imageReader);
        createBricks(imageReader);
        initiateUI(imageReader);
    }

    private static void restartBall(Ball ball, Vector2 windowDimensions) {
        Utils.randomizeBallVelocity(ball, GameConstants.PRIMARY_BALL_SPEED);
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    private void addBackground(ImageReader imageReader) {
        Renderable background = imageReader.readImage(
                GameConstants.BACKGROUND_ASSET_PATH, false);
        GameObject backgroundObject = new GameObject(
                Vector2.ZERO, windowDimensions, background);
        backgroundObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    private void initiateUI(ImageReader imageReader) {
        userInterface = new UserInterface(
                new Vector2(20, windowDimensions.y() - 60),
                new Vector2(4, 2),
                UI_GRID_ELEMENT_DIMENSIONS,
                this,
                MAX_LIFE_COUNT);
        // Initializing the hearts for the start of the game
        for (int iter = 0; iter < DEFAULT_GAME_COUNT; iter++) {
            addLife();
        }
    }

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

    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage(
                GameConstants.BRICK_ASSET_PATH, false);

        // Calculating the amount of pixels that the bricks can take up
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
                UI_GRID_ELEMENT_DIMENSIONS,
                ball,
                userPaddle,
                GameConstants.PUCK_BALL_COUNT
        );

        // Creating the bricks, row after row
        for (int row = 0; row < brickRowCount; row++) {
            for (int col = 0; col < brickCountPerRow; col++) {
                brickGrid.addObject(col, row, brickImage, strategyFactory.createRandomStrategy());
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        endgameHandler();
    }

    public void addLife() {
        if (lifeCounter < MAX_LIFE_COUNT) {
            userInterface.addHeart(imageReader);
            lifeCounter++;
        }
    }

    public boolean removeLife() {
        if (lifeCounter > 0) {
            userInterface.removeHeart();
            lifeCounter--;
        }

        return 0 == lifeCounter;
    }

    private void endgameHandler() {
        String prompt;
        // Losing scenario - Ball left the window (from below)
        if (ball.getCenter().y() > windowDimensions.y()) {

            if (!removeLife()) {
                restartBall(ball, windowDimensions);
                return;
            }

            prompt = "You lose! Play again?";
        }
        // Winning scenario - No bricks remain or user pressed 'W' button
        else if (0 == brickGrid.getBrickCount() || userInputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = "You win! Play again?";
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

    public static void main(String[] args) {
        BrickerGameManager gameManager;

        if (2 > args.length) {
            gameManager = new BrickerGameManager(
                    "Bricker",
                    new Vector2(700, 500),
                    DEFAULT_BRICK_COUNT_PER_ROW,
                    DEFAULT_BRICK_ROW_COUNT);
        }
        else if (2 == args.length) {
            gameManager = new BrickerGameManager(
                    "Bricker",
                    new Vector2(700, 500),
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

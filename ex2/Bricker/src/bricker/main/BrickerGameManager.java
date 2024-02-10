package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.Paddle;
import bricker.gameobjects.UserInterface;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final Vector2 UI_GRID_ELEMENT_DIMENSIONS = new Vector2(20, 20);
    private static final int DEFAULT_GAME_COUNT = 3;
    private static final int BRICK_DISTANCING_PIXELS = 3;
    private static final int BRICK_HEIGHT_PIXELS = 15;
    private static final int DEFAULT_BRICK_COUNT_PER_ROW = 8;
    private static final int DEFAULT_BRICK_ROW_COUNT = 7;
    private static final float WALL_WIDTH_PIXELS = 10.0f;
    private static final float BALL_SPEED = 200.0f;
    // Where in the window we start placing bricks
    private static final Vector2 BRICK_BASE_POSITION =
            new Vector2(WALL_WIDTH_PIXELS*2, WALL_WIDTH_PIXELS*2);

    private final int brickCountPerRow;
    private final int brickRowCount;
    private Ball ball;
    private BrickGrid brickGrid;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private int currentGameIndex = 1;
    private UserInterface userInterface;
    private UserInputListener userInputListener;

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
        this.windowController = windowController;
        this.userInputListener = inputListener;
        windowDimensions = windowController.getWindowDimensions();
        currentGameIndex = 1; // Restarting the game count

        // Collision rules in the foreground should not exist (interaction between bricks etc.)
        gameObjects().layers().shouldLayersCollide(
                Layer.FOREGROUND, Layer.FOREGROUND, false);

        gameObjects().layers().shouldLayersCollide(
                Layer.DEFAULT, Layer.FOREGROUND, true);


        // Creating the Ball
        Renderable ballImage =
                imageReader.readImage("asserts/ball.png", true);
        Sound collisionSound = soundReader.readSound(
                "asserts/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);
        restartBall();
        this.gameObjects().addGameObject(ball);

        Renderable paddleImage = imageReader.readImage(
                "asserts/paddle.png", true);

        // Create User Paddle
        GameObject userPaddle =
                new Paddle(
                        Vector2.ZERO,
                        new Vector2(100, 15),
                        windowDimensions,
                        paddleImage,
                        inputListener);
        userPaddle.setCenter(
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 40));

        this.gameObjects().addGameObject(userPaddle);

        // Instantiating the rest of the objects
        createBoardWalls();
        addBackground(imageReader);
        createBricks(imageReader);
        initiateUI(imageReader);
    }

    private void restartBall() {
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
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    private void addBackground(ImageReader imageReader) {
        Renderable background = imageReader.readImage(
                "asserts/DARK_BG2_small.jpeg", false);
        GameObject backgroundObject = new GameObject(
                Vector2.ZERO, windowDimensions, background);
        backgroundObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    private void initiateUI(ImageReader imageReader) {
        userInterface = new UserInterface(
                new Vector2(20, windowDimensions.y() - 60),
                new Vector2(3, 2),
                UI_GRID_ELEMENT_DIMENSIONS,
                this,
                DEFAULT_GAME_COUNT);
        // Initializing the hearts for the start of the game
        for (int iter = 0; iter < DEFAULT_GAME_COUNT; iter++) {
            userInterface.addHeart(imageReader);
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
            this.gameObjects().addGameObject(wallObject);
        }
    }

    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage(
                "asserts/brick.png", false);

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

        // Creating the bricks, row after row
        for (int row = 0; row < brickRowCount; row++) {
            for (int col = 0; col < brickCountPerRow; col++) {
                brickGrid.addObject(col, row, brickImage, new BasicCollisionStrategy(this, brickGrid));
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        endgameHandler();
    }

    private void endgameHandler() {
        String prompt;
        // Losing scenario - Ball left the window (from below)
        if (ball.getCenter().y() > windowDimensions.y()) {

            if (currentGameIndex < DEFAULT_GAME_COUNT) {
                currentGameIndex++;
                restartBall();
                userInterface.removeHeart();
                return;
            }

            prompt = "You lose! Play again?";
        }
        // Winning scenario - No bricks or user pressed 'W' button
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

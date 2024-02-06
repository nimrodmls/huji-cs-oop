package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final int BRICK_DISTANCING_PIXELS = 3;
    private static final int BRICK_HEIGHT_PIXELS = 15;
    private static final int DEFAULT_BRICK_COUNT_PER_ROW = 8;
    private static final int DEFAULT_BRICK_ROW_COUNT = 7;
    private static final float WALL_WIDTH_PIXELS = 10.0f;
    private static final float BALL_SPEED = 200.0f;
    // Where in the window we start placing bricks
    private static final Vector2 BRICK_BASE_POSITION = new Vector2(WALL_WIDTH_PIXELS, WALL_WIDTH_PIXELS);

    private final int brickCountPerRow;
    private final int brickRowCount;

    public BrickerGameManager(String title, Vector2 windowSize, int brickCountPerRow, int brickRowCount) {
        super(title, windowSize);
        this.brickCountPerRow = brickCountPerRow;
        this.brickRowCount = brickRowCount;
    }

    public void removeGameObject(GameObject object) {
        gameObjects().removeGameObject(object);
    }

    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader,
            UserInputListener inputListener,
            WindowController windowController) {

        super.initializeGame(
                imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();

        // Create Ball

        Renderable ballImage =
                imageReader.readImage("asserts/ball.png", true);
        Sound collisionSound = soundReader.readSound(
                "asserts/blop_cut_silenced.wav");
        GameObject ballObject =
                new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);

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
        ballObject.setVelocity(new Vector2(ballSpeedX, ballSpeedY));
        ballObject.setCenter(windowDimensions.mult(0.5f));

        this.gameObjects().addGameObject(ballObject);

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
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));

        this.gameObjects().addGameObject(userPaddle);

        // Create CPU Paddle
        GameObject cpuPaddle =
                new GameObject(Vector2.ZERO, new Vector2(100, 15), paddleImage);
        cpuPaddle.setCenter(
                new Vector2(windowDimensions.x() / 2, 30));

        this.gameObjects().addGameObject(cpuPaddle);

        createBoardWalls(windowDimensions);
        addBackground(imageReader, windowDimensions);
        createBricks(imageReader, windowDimensions);
    }

    private void addBackground(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable background = imageReader.readImage(
                "asserts/DARK_BG2_small.jpeg", false);
        GameObject backgroundObject = new GameObject(
                Vector2.ZERO, windowDimensions, background);
        backgroundObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    private void createBoardWalls(Vector2 windowDimensions) {
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

    private void createBricks(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable brickImage = imageReader.readImage(
                "asserts/brick.png", false);

        // Calculating the amount of pixels that the bricks can take up
        // this does not include the walls, since the player cannot reach
        float maxRowLength = windowDimensions.x() - (WALL_WIDTH_PIXELS * 2);
        // Calculating the width of a single brick, in the calculation we take spaces
        // between the bricks into account
        float brickWidth =
                (maxRowLength - ((brickCountPerRow - 1) * BRICK_DISTANCING_PIXELS)) / brickCountPerRow;

        // Creating the bricks, row after row
        for (int row = 0; row < brickRowCount; row++) {
            for (int col = 0; col < brickCountPerRow; col++) {

                Vector2 brickPosition = BRICK_BASE_POSITION.add(new Vector2(
                        col * (brickWidth + BRICK_DISTANCING_PIXELS),
                        row * (BRICK_HEIGHT_PIXELS + BRICK_DISTANCING_PIXELS)));

                GameObject brickObject = new Brick(
                        brickPosition,
                        new Vector2(brickWidth, BRICK_HEIGHT_PIXELS),
                        brickImage,
                        new BasicCollisionStrategy(this));

                this.gameObjects().addGameObject(brickObject);
            }
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

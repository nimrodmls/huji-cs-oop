package bricker.main;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BrickerGameManager extends GameManager {

    private final float WALL_WIDTH_PIXELS = 10.0f;

    public BrickerGameManager(String title, Vector2 windowSize) {
        super(title, windowSize);
    }

    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader,
            UserInputListener inputListener,
            WindowController windowController) {

        super.initializeGame(
                imageReader, soundReader, inputListener, windowController);

        // Create Ball

        Renderable ballImage =
                imageReader.readImage("asserts/ball.png", true);
        Sound collisionSound = soundReader.readSound(
                "asserts/blop_cut_silenced.wav");
        GameObject ballObject =
                new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);

        ballObject.setVelocity(Vector2.DOWN.mult(200));
        Vector2 windowDimensions = windowController.getWindowDimensions();
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

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager(
                "Bricker", new Vector2(700, 500));
        gameManager.run();
    }
}

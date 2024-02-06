package bricker.main;

import bricker.gameobjects.Ball;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

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
        GameObject ballObject =
                new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage);

        ballObject.setVelocity(Vector2.DOWN.mult(200));
        Vector2 windowDimensions = windowController.getWindowDimensions();
        ballObject.setCenter(windowDimensions.mult(0.5f));

        this.gameObjects().addGameObject(ballObject);

        Renderable paddleImage = imageReader.readImage(
                "asserts/paddle.png", true);
        int[] paddleHeights = new int[] {(int)windowDimensions.y() - 30, 30};

        for (int height : paddleHeights) {
            GameObject paddleObject =
                    new GameObject(Vector2.ZERO, new Vector2(100, 15), paddleImage);
            paddleObject.setCenter(
                    new Vector2(windowDimensions.x() / 2, height));

            this.gameObjects().addGameObject(paddleObject);
        }

        createBoardWalls(windowDimensions);
        addBackground(imageReader, windowDimensions);
    }

    private void addBackground(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable background = imageReader.readImage(
                "asserts/DARK_BG2_small.jpeg", false);
        GameObject backgroundObject = new GameObject(
                Vector2.ZERO, windowDimensions, background);
        this.gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    private void createBoardWalls(Vector2 windowDimensions) {
        Vector2 horizontalWall = new Vector2(windowDimensions.x(), WALL_WIDTH_PIXELS);
        Vector2 verticalWall = new Vector2(WALL_WIDTH_PIXELS, windowDimensions.y());
        // Defining Position-Wall Pairs
        Vector2[][] wallPositions = new Vector2[][] {
                { Vector2.ZERO, horizontalWall },
                { new Vector2(0, windowDimensions.y() - WALL_WIDTH_PIXELS), horizontalWall },
                { Vector2.ZERO, verticalWall },
                { new Vector2(windowDimensions.x() - WALL_WIDTH_PIXELS, 0), verticalWall }
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

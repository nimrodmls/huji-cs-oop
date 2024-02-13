package bricker.brick_strategies;

import danogl.collisions.Layer;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.GameObject;

import bricker.gameobjects.FallingHeart;
import bricker.gameobjects.Paddle;
import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;

/**
 * Represents a collision strategy for a falling heart brick.
 * This type of brick will spawn a falling heart, which will give the player an extra life.
 * @author Nimrod M.
 */
public class FallingHeartStrategy extends BasicCollisionStrategy {
    private static final Vector2 HEART_VELOCITY = new Vector2(0, 100.0f);

    private final BrickerGameManager gameManager;
    private final ImageRenderable heartImage;
    private final Vector2 heartDimensions;
    private final Paddle paddle;

    
    /**
     * Constructs a FallingHeartStrategy object.
     * 
     * @param gameManager       the game manager
     * @param brickGrid         the brick grid
     * @param heartImage        the image of the heart to render
     * @param heartDimensions   the dimensions of the heart to render
     * @param paddle            the paddle, used to detect collisions with the heart
     */
    public FallingHeartStrategy(BrickerGameManager gameManager, BrickGrid brickGrid, ImageRenderable heartImage, Vector2 heartDimensions, Paddle paddle) {
        super(brickGrid);
        this.gameManager = gameManager;
        this.heartImage = heartImage;
        this.heartDimensions = heartDimensions;
        this.paddle = paddle;
    }

    /**
     * Handles the collision with the falling heart brick.
     * This will create the falling heart.
     * 
     * @param object1 the first game object involved in the collision
     * @param object2 the second game object involved in the collision
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        FallingHeart fallingHeart = new FallingHeart(object1.getCenter(), heartDimensions, heartImage, paddle, gameManager);
        fallingHeart.setVelocity(HEART_VELOCITY);
        gameManager.addGameObject(fallingHeart, Layer.DEFAULT);
    }
}

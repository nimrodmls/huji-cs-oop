package bricker.brick_strategies;

import bricker.gameobjects.FallingHeart;
import bricker.gameobjects.Paddle;
import danogl.GameObject;
import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;
import danogl.collisions.Layer;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class FallingHeartStrategy extends BasicCollisionStrategy {
    private static final Vector2 HEART_VELOCITY = new Vector2(0, 100.0f);

    private final BrickerGameManager gameManager;
    private final ImageRenderable heartImage;
    private final Vector2 heartDimensions;
    private final Paddle paddle;

    public FallingHeartStrategy(BrickerGameManager gameManager, BrickGrid brickGrid, ImageRenderable heartImage, Vector2 heartDimensions, Paddle paddle) {
        super(gameManager, brickGrid);
        this.gameManager = gameManager;
        this.heartImage = heartImage;
        this.heartDimensions = heartDimensions;
        this.paddle = paddle;
    }

    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        FallingHeart fallingHeart = new FallingHeart(object1.getCenter(), heartDimensions, heartImage, paddle, gameManager);
        fallingHeart.setVelocity(HEART_VELOCITY);
        gameManager.addGameObject(fallingHeart, Layer.DEFAULT);
    }
}

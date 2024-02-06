package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class BasicCollisionStrategy implements CollisionStrategy {

    private final BrickerGameManager gameManager;

    public BasicCollisionStrategy(BrickerGameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        gameManager.removeGameObject(object1, Layer.FOREGROUND);
    }
}

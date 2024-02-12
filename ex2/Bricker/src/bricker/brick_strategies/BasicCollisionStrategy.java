package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class BasicCollisionStrategy implements CollisionStrategy {

    private final BrickGrid brickGrid;

    public BasicCollisionStrategy(BrickGrid brickGrid) {
        this.brickGrid = brickGrid;
    }
    
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        brickGrid.removeObject(object1);
    }
}

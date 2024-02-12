package bricker.brick_strategies;

import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class DoubleActionStrategy extends BasicCollisionStrategy {
    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    public DoubleActionStrategy(BrickerGameManager gameManager, BrickGrid brickGrid, CollisionStrategy strategy1, CollisionStrategy strategy2) {
        super(gameManager, brickGrid);
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);
        strategy1.onCollision(object1, object2);
        strategy2.onCollision(object1, object2);
    }
}

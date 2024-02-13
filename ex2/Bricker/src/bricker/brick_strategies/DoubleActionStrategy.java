package bricker.brick_strategies;

import danogl.GameObject;

import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;

/**
 * Represents a collision strategy that combines two collision strategies into one.
 * When a collision occurs, both strategies will be executed.
 * @author Nimrod M.
 */
public class DoubleActionStrategy extends BasicCollisionStrategy {
    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    /**
     * Constructs a DoubleActionStrategy with the two collision strategies.
     *
     * @param brickGrid   the brick grid to apply the collision strategy on
     * @param strategy1   the first collision strategy to execute
     * @param strategy2   the second collision strategy to execute
     */
    public DoubleActionStrategy(BrickGrid brickGrid, CollisionStrategy strategy1, CollisionStrategy strategy2) {
        super(brickGrid);
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    /**
     * Executes the collision strategies when a collision occurs.
     *
     * @param object1   the first game object involved in the collision
     * @param object2   the second game object involved in the collision
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);
        strategy1.onCollision(object1, object2);
        strategy2.onCollision(object1, object2);
    }
}

package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;

import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.main.BrickerGameManager;

/**
 * A basic collision strategy implementation that removes the collided brick.
 * @author Nimrod M.
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    private final BrickGrid brickGrid;

    /**
     * Constructs a BasicCollisionStrategy with the specified brick grid.
     * The strategy assumes that all collided bricks belong to the given grid.
     * 
     * @param brickGrid the brick grid to be used for collision handling
     */
    public BasicCollisionStrategy(BrickGrid brickGrid) {
        this.brickGrid = brickGrid;
    }
    
    /**
     * Handles the collision and removal with the brick.
     * 
     * @param object1 the first game object involved in the collision
     * @param object2 the second game object involved in the collision
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        brickGrid.removeObject(object1);
    }
}

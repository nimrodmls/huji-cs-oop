package bricker.brick_strategies;

import danogl.GameObject;

/**
 * This interface represents a collision strategy between two game objects.
 * @author Nimrod M.
 */
public interface CollisionStrategy {
    
    /**
     * This method is called when a collision occurs between two game objects.
     *
     * @param object1 the first game object involved in the collision
     * @param object2 the second game object involved in the collision
     */
    void onCollision(GameObject object1, GameObject object2);
}

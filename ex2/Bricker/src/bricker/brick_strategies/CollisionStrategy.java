package bricker.brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy {
    void onCollision(GameObject object1, GameObject object2);
}

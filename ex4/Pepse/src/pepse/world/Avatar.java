package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;
import pepse.util.GameConstants;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;

    private final UserInputListener inputListener;
    private float energy = GameConstants.AVATAR_MAX_ENERGY;

    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(
            pos,
            GameConstants.AVATAR_SIZE,
            imageReader.readImage(GameConstants.IDLE_AVATAR_IMG_PATH, true)
        );
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GameConstants.GRAVITY);
        this.inputListener = inputListener;
    }

    public float getEnergy() {
        return energy;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (consumeEnergy(GameConstants.AVATAR_MOVE_ENERGY_COST)) {
                xVel -= VELOCITY_X;
            }
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (consumeEnergy(GameConstants.AVATAR_MOVE_ENERGY_COST)) {
                xVel += VELOCITY_X;
            }
        }

        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            if (consumeEnergy(GameConstants.AVATAR_JUMP_ENERGY_COST)) {
                transform().setVelocityY(VELOCITY_Y);
            }
        }

        // Regenerate energy if not moving
        if (getVelocity().isZero()) {
            if (energy < GameConstants.AVATAR_MAX_ENERGY) {
                energy += Math.min(1, GameConstants.AVATAR_MAX_ENERGY - energy);
            }
        }
    }

    public boolean consumeEnergy(float amount) {
        if (energy >= amount) {
            energy -= amount;
            return true;
        }
        return false;
    }
}

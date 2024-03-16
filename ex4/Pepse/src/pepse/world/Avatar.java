package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.util.Dispatcher;
import pepse.util.GameConstants;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;

    private final AnimationRenderable avatarIdleAnimation;
    private final AnimationRenderable avatarJumpAnimation;
    private final AnimationRenderable avatarRunAnimation;
    private final UserInputListener inputListener;
    private final Dispatcher dispatcher;
    private float energy = GameConstants.AVATAR_MAX_ENERGY;

    public Avatar(Vector2 pos,
                  UserInputListener inputListener,
                  ImageReader imageReader,
                  Dispatcher dispatcher) {
        super(
            pos,
            GameConstants.AVATAR_SIZE,
            imageReader.readImage(GameConstants.IDLE_ANIMATION_PATHS[0], true)
        );

        this.dispatcher = dispatcher;
        this.inputListener = inputListener;

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GameConstants.GRAVITY);

        // Initializing the animation renderers once!
        avatarIdleAnimation = new AnimationRenderable(
                GameConstants.IDLE_ANIMATION_PATHS,
                imageReader,
                true,
                0.5f);

        avatarRunAnimation = new AnimationRenderable(
                GameConstants.RUN_ANIMATION_PATHS,
                imageReader,
                true,
                0.5f);

        avatarJumpAnimation = new AnimationRenderable(
                GameConstants.JUMP_ANIMATION_PATHS,
                imageReader,
                true,
                0.5f);

        renderer().setRenderable(avatarIdleAnimation);
        dispatcher.createEvent(GameConstants.AVATAR_JUMP_EVENT);
    }

    public float getEnergy() {
        return energy;
    }

    public void addEnergy(float energy) {
        if (this.energy + energy > GameConstants.AVATAR_MAX_ENERGY) {
            this.energy = GameConstants.AVATAR_MAX_ENERGY;
            return;
        }
        this.energy += energy;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (consumeEnergy(GameConstants.AVATAR_MOVE_ENERGY_COST)) {
                xVel -= VELOCITY_X;
                renderer().setRenderable(avatarRunAnimation);
                renderer().setIsFlippedHorizontally(true);
            }
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (consumeEnergy(GameConstants.AVATAR_MOVE_ENERGY_COST)) {
                xVel += VELOCITY_X;
                renderer().setRenderable(avatarRunAnimation);
                renderer().setIsFlippedHorizontally(false);
            }
        }

        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            if (consumeEnergy(GameConstants.AVATAR_JUMP_ENERGY_COST)) {
                transform().setVelocityY(VELOCITY_Y);
                renderer().setRenderable(avatarJumpAnimation);
                // Notifying the dispatcher that jump has occurred
                dispatcher.setEvent(GameConstants.AVATAR_JUMP_EVENT);
            }
        }

        // Regenerate energy if not moving
        if (getVelocity().isZero()) {
            if (energy < GameConstants.AVATAR_MAX_ENERGY) {
                energy += Math.min(1, GameConstants.AVATAR_MAX_ENERGY - energy);
            }
            renderer().setRenderable(avatarIdleAnimation);
        }
    }

    private boolean consumeEnergy(float amount) {
        if (energy >= amount) {
            energy -= amount;
            return true;
        }
        return false;
    }
}

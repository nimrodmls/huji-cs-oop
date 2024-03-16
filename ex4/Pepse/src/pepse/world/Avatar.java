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
    private static final float ANIMATION_CLIP_DURATION = 0.5f;

    private AnimationRenderable avatarIdleAnimation;
    private AnimationRenderable avatarJumpAnimation;
    private AnimationRenderable avatarRunAnimation;
    private final UserInputListener inputListener;
    private final Dispatcher dispatcher;
    private float energy = GameConstants.AVATAR_MAX_ENERGY;

    public Avatar(Vector2 startPosition,
                  UserInputListener inputListener,
                  ImageReader imageReader,
                  Dispatcher dispatcher) {
        // It doesn't matter which renderable we pass here, it will be replaced by the idle animation
        // once initialized
        super(
            startPosition,
            GameConstants.AVATAR_SIZE,
            imageReader.readImage(GameConstants.IDLE_ANIMATION_PATHS[0], true)
        );

        this.dispatcher = dispatcher;
        this.inputListener = inputListener;

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GameConstants.WORLD_GRAVITY);

        // Initializing the animation renderers once!
        initializeAnimations(imageReader);

        // The avatar is initialized as idle
        renderer().setRenderable(avatarIdleAnimation);

        // Creating the jump event, it will be triggered when the avatar jumps
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
        float xVelocity = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (consumeEnergy(GameConstants.AVATAR_MOVE_ENERGY_COST)) {
                xVelocity -= VELOCITY_X;
                renderer().setRenderable(avatarRunAnimation);
                renderer().setIsFlippedHorizontally(true);
            }
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (consumeEnergy(GameConstants.AVATAR_MOVE_ENERGY_COST)) {
                xVelocity += VELOCITY_X;
                renderer().setRenderable(avatarRunAnimation);
                renderer().setIsFlippedHorizontally(false);
            }
        }

        transform().setVelocityX(xVelocity);
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

    private void initializeAnimations(ImageReader imageReader) {
        avatarIdleAnimation = new AnimationRenderable(
                GameConstants.IDLE_ANIMATION_PATHS,
                imageReader,
                true,
                ANIMATION_CLIP_DURATION);

        avatarRunAnimation = new AnimationRenderable(
                GameConstants.RUN_ANIMATION_PATHS,
                imageReader,
                true,
                ANIMATION_CLIP_DURATION);

        avatarJumpAnimation = new AnimationRenderable(
                GameConstants.JUMP_ANIMATION_PATHS,
                imageReader,
                true,
                ANIMATION_CLIP_DURATION);
    }
}

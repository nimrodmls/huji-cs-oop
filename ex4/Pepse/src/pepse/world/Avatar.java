package pepse.world;

import java.awt.event.KeyEvent;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;

import pepse.util.Dispatcher;
import pepse.util.GameConstants;

/**
 * The player's character in the game. The avatar can move left and right, and jump.
 * The avatar has an energy property, which is used to perform actions.
 * The avatar can consume consumable objects, and can be consumed by other objects.
 * @author Nimrod M.
 */
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

    /**
     * Construct a new avatar - The avatar is initialized in idle state.
     * @param startPosition The starting position of the avatar in the game world
     * @param inputListener The input listener that will be used to control the avatar
     * @param imageReader The image reader that will be used to read the avatar's images
     * @param dispatcher The dispatcher that will be used to register events
     */
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

    /**
     * @return The current energy value of the avatar
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * Add energy to the avatar - If the energy value exceeds the maximum energy value,
     * the energy value will be set to the maximum energy value, with no extras.
     * @param energy The energy value to ADD to the avatar
     */
    public void addEnergy(float energy) {
        if (this.energy + energy > GameConstants.AVATAR_MAX_ENERGY) {
            this.energy = GameConstants.AVATAR_MAX_ENERGY;
            return;
        }
        this.energy += energy;
    }

    /**
     * Update the avatar's state according to the user's input.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVelocity = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (consumeEnergy(GameConstants.AVATAR_RUN_ENERGY_COST)) {
                xVelocity -= VELOCITY_X;
                renderer().setRenderable(avatarRunAnimation);
                renderer().setIsFlippedHorizontally(true);
            }
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (consumeEnergy(GameConstants.AVATAR_RUN_ENERGY_COST)) {
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

    /**
     * Consume energy from the avatar - If the avatar has enough energy, the energy
     * will be consumed and the function will return true. Otherwise, the function
     * will return false.
     * @param amount The amount of energy to consume
     * @return true if the energy was consumed, false otherwise
     */
    private boolean consumeEnergy(float amount) {
        if (energy >= amount) {
            energy -= amount;
            return true;
        }
        return false;
    }

    /**
     * Initialize the avatar's animations.
     * @param imageReader The image reader that will be used to read the avatar's images
     */
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

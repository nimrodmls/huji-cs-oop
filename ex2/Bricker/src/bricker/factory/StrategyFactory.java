package bricker.factory;

import java.util.Random;

import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import bricker.brick_strategies.*;
import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.Paddle;
import bricker.main.BrickerGameManager;
import bricker.utilities.GameConstants;

/**
 * A factory class for creating collision strategies for the bricks in the game.
 * @author Nimrod M.
 */
public class StrategyFactory {
    private final BrickerGameManager gameManager;
    private final BrickGrid brickGrid;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final Ball primaryBall;
    private final Paddle primaryPaddle;

    /**
     * @param gameManager       the responsible game manager
     * @param brickGrid         the brick grid for the bricks
     * @param imageReader       an image reader
     * @param soundReader       a sound reader
     * @param inputListener     a user input listener
     * @param windowDimensions  the dimensions of the game window
     * @param primaryBall       the primary ball in the game
     * @param primaryPaddle     the primary (user's) paddle in the game
     */
    public StrategyFactory(BrickerGameManager gameManager,
                           BrickGrid brickGrid,
                           ImageReader imageReader,
                           SoundReader soundReader,
                           UserInputListener inputListener,
                           Vector2 windowDimensions,
                           Ball primaryBall,
                           Paddle primaryPaddle) {

        this.gameManager = gameManager;
        this.brickGrid = brickGrid;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.primaryBall = primaryBall;
        this.primaryPaddle = primaryPaddle;
    }

    /**
     * Creates a new collision strategy for a brick.
     * This strategy is created randomly, with 50-50 chance of it being special, or regular.
     * When special is chosen, a special strategy is chosen randomly in a uniform manner.
     *
     * @return a new collision strategy for a brick
     */
    public CollisionStrategy createRandomStrategy() {
        Random random = new Random();
        boolean isSpecial = random.nextBoolean();

        if (isSpecial) {
            // We were chosen to create a special strategy

            // Initializing a counter for the double actions - the code will not create
            // nested double actions once this counter has reached 0
            Counter allowedDoubleActions = new Counter();
            allowedDoubleActions.increaseBy(GameConstants.ALLOWED_DOUBLE_ACTIONS);
            return createRandomSpecialStrategy(allowedDoubleActions);

        } else {
            // Creating a regular brick strategy
            return new BasicCollisionStrategy(brickGrid);
        }
    }

    /**
     * Creates a new <b>special collision</b> strategy for a brick.
     * It is chosen randomly, in a uniform manner.
     *
     * @param allowedDoubleActions specifies how many double actions are allowed to be created
     * @return the new special collision strategy
     */
    public CollisionStrategy createRandomSpecialStrategy(Counter allowedDoubleActions) {
        SpecialStrategies[] availableStrategies = getSpecialStrategies(allowedDoubleActions);

        Random random = new Random();
        int chosenStrategy = random.nextInt(availableStrategies.length);

        CollisionStrategy strategy;
        switch (availableStrategies[chosenStrategy]) {
            case STRATEGY_PUCK:
                ImageRenderable ballImage =
                        imageReader.readImage(GameConstants.PUCK_BALL_ASSET_PATH, true);
                Sound collisionSound = soundReader.readSound(
                        GameConstants.COLLISION_SOUND_PATH);
                strategy = new PuckStrategy(
                        gameManager,
                        brickGrid,
                        GameConstants.PUCK_BALL_COUNT,
                        collisionSound,
                        ballImage
                );
                break;

            case STRATEGY_CAMERA:
                strategy = new CameraStrategy(
                        gameManager,
                        brickGrid,
                        primaryBall,
                        windowDimensions);
                break;

            case STRATEGY_FALLING_HEART:
                ImageRenderable heartImage = imageReader.readImage(
                        GameConstants.HEART_ASSET_PATH, true);
                strategy = new FallingHeartStrategy(
                        gameManager,
                        brickGrid,
                        heartImage,
                        GameConstants.FALLING_HEART_DIMENSIONS,
                        primaryPaddle);
                break;

            case STRATEGY_DOUBLE_PADDLE:
                ImageRenderable paddleImage = imageReader.readImage(
                        GameConstants.PADDLE_ASSET_PATH, false);
                strategy = new DoublePaddleStrategy(
                        gameManager,
                        brickGrid,
                        inputListener,
                        paddleImage,
                        windowDimensions);
                break;

            case STRATEGY_DOUBLE_ACTION:
                allowedDoubleActions.decrement();
                CollisionStrategy strategy1 = createRandomSpecialStrategy(allowedDoubleActions);
                CollisionStrategy strategy2 = createRandomSpecialStrategy(allowedDoubleActions);
                strategy = new DoubleActionStrategy(brickGrid, strategy1, strategy2);
                break;

            default:
                // This shouldn't happen...
                strategy = null;
        }

        return strategy;
    }

    /**
     * Returns an array of allowed special strategies.
     *
     * @param allowedDoubleActions the counter for the number of double actions allowed,
     *                             if this is 0, then the double action strategy will not be allowed
     * @return an array of allowed special strategy IDs
     */
    private static SpecialStrategies[] getSpecialStrategies(Counter allowedDoubleActions) {
        SpecialStrategies[] availableStrategies = SpecialStrategies.values();

        // If double actions are not allowed, we don't let it get generated randomly,
        // therefore selecting from all the other strategies
        if (0 == allowedDoubleActions.value()) {
            availableStrategies = new SpecialStrategies[] {
                SpecialStrategies.STRATEGY_PUCK,
                SpecialStrategies.STRATEGY_FALLING_HEART,
                SpecialStrategies.STRATEGY_DOUBLE_PADDLE,
                SpecialStrategies.STRATEGY_CAMERA
            };
        }

        return availableStrategies;
    }
}

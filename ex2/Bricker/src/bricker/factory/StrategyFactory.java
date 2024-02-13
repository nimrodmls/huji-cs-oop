package bricker.factory;

import bricker.brick_strategies.*;
import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.Paddle;
import bricker.main.BrickerGameManager;
import bricker.utilities.GameConstants;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

public class StrategyFactory {

    private static final int ALLOWED_DOUBLE_ACTIONS = 2;

    private final BrickerGameManager gameManager;
    private final BrickGrid brickGrid;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final Vector2 fallingHeartDimensions;
    private final Ball primaryBall;
    private final Paddle primaryPaddle;
    private final int puckBallCount;

    private final Counter doublePaddleCounter;
    private final Counter doublePaddleHitCounter;
    private final Counter cameraHitCounter;

    public StrategyFactory(BrickerGameManager gameManager,
                           BrickGrid brickGrid,
                           ImageReader imageReader,
                           SoundReader soundReader,
                           UserInputListener inputListener,
                           Vector2 windowDimensions,
                           Vector2 fallingHeartDimensions,
                           Ball primaryBall,
                           Paddle primaryPaddle,
                           int PuckBallCount) {

        this.gameManager = gameManager;
        this.brickGrid = brickGrid;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.fallingHeartDimensions = fallingHeartDimensions;
        this.primaryBall = primaryBall;
        this.primaryPaddle = primaryPaddle;
        puckBallCount = PuckBallCount;

        doublePaddleCounter = new Counter();
        doublePaddleHitCounter = new Counter();
        cameraHitCounter = new Counter();
    }

    public CollisionStrategy createRandomStrategy() {
        Random random = new Random();
        boolean isSpecial = random.nextBoolean();

        // There is a 50-50 chance for either getting the basic behavior or a special behavior
        if (isSpecial) {
            // We were chosen to create a special strategy
            // Initializing a counter for the double actions - the code will not create
            // nested double actions once this counter has reached 0
            Counter allowedDoubleActions = new Counter();
            allowedDoubleActions.increaseBy(ALLOWED_DOUBLE_ACTIONS);
            return createRandomSpecialStrategy(allowedDoubleActions);

        } else {
            // Creating a regular brick strategy
            return new BasicCollisionStrategy(brickGrid);
        }
    }

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
                        puckBallCount,
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
                        fallingHeartDimensions,
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

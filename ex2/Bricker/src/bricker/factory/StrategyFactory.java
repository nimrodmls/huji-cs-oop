package bricker.factory;

import bricker.brick_strategies.*;
import bricker.gameobjects.Ball;
import bricker.gameobjects.BrickGrid;
import bricker.gameobjects.Paddle;
import bricker.main.BrickerGameManager;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

enum SpecialStrategy {
    STRATEGY_PUCK(1),
    STRATEGY_FALLING_HEART(2),
    STRATEGY_DOUBLE_PADDLE(3),
    STRATEGY_CAMERA(4),
    STRATEGY_DOUBLE_ACTION(5);

    private final int strategyId;

    SpecialStrategy(int strategyId) {
        this.strategyId = strategyId;
    }

    public int strategyId() {
        return strategyId;
    }
}

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

        if (isSpecial) {
            Counter allowedDoubleActions = new Counter();
            allowedDoubleActions.increaseBy(ALLOWED_DOUBLE_ACTIONS);
            return createRandomSpecialStrategy(allowedDoubleActions);

        } else {
            return new BasicCollisionStrategy(gameManager, brickGrid);
        }
    }

    public CollisionStrategy createRandomSpecialStrategy(Counter allowedDoubleActions) {
        SpecialStrategy[] availableStrategies = getSpecialStrategies(allowedDoubleActions);

        Random random = new Random();
        int chosenStrategy = random.nextInt(availableStrategies.length);

        CollisionStrategy strategy;
        switch (availableStrategies[chosenStrategy]) {
            case STRATEGY_PUCK:
                ImageRenderable ballImage =
                        imageReader.readImage("asserts/mockBall.png", true);
                Sound collisionSound = soundReader.readSound(
                        "asserts/blop_cut_silenced.wav");
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
                        windowDimensions,
                        cameraHitCounter);
                break;

            case STRATEGY_FALLING_HEART:
                ImageRenderable heartImage = imageReader.readImage(
                        "asserts/heart.png", true);
                strategy = new FallingHeartStrategy(
                        gameManager,
                        brickGrid,
                        heartImage,
                        fallingHeartDimensions,
                        primaryPaddle);
                break;

            case STRATEGY_DOUBLE_PADDLE:
                ImageRenderable paddleImage = imageReader.readImage(
                        "asserts/paddle.png", false);
                strategy = new DoublePaddleStrategy(
                        gameManager,
                        brickGrid,
                        inputListener,
                        paddleImage,
                        windowDimensions,
                        doublePaddleCounter,
                        doublePaddleHitCounter);
                break;

            case STRATEGY_DOUBLE_ACTION:
                allowedDoubleActions.decrement();
                CollisionStrategy strategy1 = createRandomSpecialStrategy(allowedDoubleActions);
                CollisionStrategy strategy2 = createRandomSpecialStrategy(allowedDoubleActions);
                strategy = new DoubleActionStrategy(gameManager, brickGrid, strategy1, strategy2);
                break;

            default:
                // This shouldn't happen...
                strategy = null;
        }

        return strategy;
    }

    private static SpecialStrategy[] getSpecialStrategies(Counter allowedDoubleActions) {
        SpecialStrategy[] availableStrategies = SpecialStrategy.values();

        // If double actions are not allowed, we don't let it get generated randomly,
        // therefore selecting from all the other strategies
        if (0 == allowedDoubleActions.value()) {
            availableStrategies = new SpecialStrategy[] {
                SpecialStrategy.STRATEGY_PUCK,
                SpecialStrategy.STRATEGY_FALLING_HEART,
                SpecialStrategy.STRATEGY_DOUBLE_PADDLE,
                SpecialStrategy.STRATEGY_CAMERA
            };
        }
        return availableStrategies;
    }
}

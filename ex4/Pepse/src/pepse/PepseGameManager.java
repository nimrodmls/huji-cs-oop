package pepse;

import java.awt.*;
import java.util.List;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import pepse.util.Dispatcher;
import pepse.util.GameConstants;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.consumables.Consumable;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

/**
 * The main game manager for the Pepse game.
 * @author Nimrod M.
 */
public class PepseGameManager extends GameManager {

    private static final float AVATAR_CORRECTION = GameConstants.AVATAR_SIZE.x();
    private static final float DAY_NIGHT_CYCLE_SECONDS = 30.0f;
    private static final int TERRAIN_SEED = 0;

    private Dispatcher eventDispatcher;
    private Avatar avatar;

    /**
     * Construct a new game manager for the Pepse Game.
     */
    public PepseGameManager() {
        super(GameConstants.WINDOW_TITLE);
    }

    /**
     * Initialize a new game.
     * Creates all the game objects, and the player's character.
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader,
            UserInputListener inputListener,
            WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // Creating a new event dispatcher for the game, it should be reset upon game restart
        // hence it's initialized here
        eventDispatcher = new Dispatcher();

        Vector2 windowDimensions = windowController.getWindowDimensions();

        // Creating all game objects, the order matters.
        createSkyline(windowDimensions);
        Terrain terrain = createTerrain(windowDimensions);
        createAvatar(windowDimensions, inputListener, imageReader);
        createFlora(windowDimensions, terrain, eventDispatcher);

        // The night sky is created in front of all GAME objects
        createNightSky(windowDimensions);

        // The UI is created in front of everything, shouldn't be affected by night/day cycle
        createUI(avatar);

    }

    /**
     * Create the skyline of the game.
     * @param windowDimensions The dimensions of the window.
     */
    private void createSkyline(Vector2 windowDimensions) {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        GameObject sun = Sun.create(windowDimensions, DAY_NIGHT_CYCLE_SECONDS);
        GameObject sunHalo = SunHalo.create(sun);

        // The sun is in front of the halo - order is important
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }

    /**
     * Create the terrain of the game.
     * @param windowDimensions The dimensions of the window.
     * @return The terrain object.
     */
    private Terrain createTerrain(Vector2 windowDimensions) {
        Terrain terrain = new Terrain(windowDimensions, TERRAIN_SEED);
        List<Block> blocks = terrain.createInRange(0, (int)windowDimensions.x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        return terrain;
    }

    /**
     * Creating the playable avatar for the game.
     * @param windowDimensions The dimensions of the window.
     * @param inputListener The user input listener for the game.
     * @param imageReader The image reader for the game.
     */
    private void createAvatar(Vector2 windowDimensions,
                              UserInputListener inputListener,
                              ImageReader imageReader) {
        // The initial position of the avatar is at the rightmost corner of the window,
        // just above the ground. The correction is due to the fact that the position given
        // is for the top left corner of the avatar, and we need it above ground & within the window.
        Vector2 avatarInitialPosition = new Vector2(
                windowDimensions.x() - AVATAR_CORRECTION,
                (GameConstants.INITIAL_GROUND_HEIGHT_FACTOR *
                        windowDimensions.y()) - AVATAR_CORRECTION);
        avatar = new Avatar(avatarInitialPosition, inputListener, imageReader, eventDispatcher);
        gameObjects().addGameObject(avatar);
    }

    /**
     * Create the flora of the game.
     * @param windowDimensions The dimensions of the window.
     * @param terrain The terrain of the game.
     * @param eventDispatcher The event dispatcher for the game.
     */
    private void createFlora(Vector2 windowDimensions,
                             Terrain terrain,
                             Dispatcher eventDispatcher) {
        Flora flora = new Flora(
                terrain::groundHeightAt,
                (GameObject obj) -> gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS),
                (GameObject obj) -> gameObjects().addGameObject(obj, Layer.DEFAULT),
                this::fruitHandler,
                eventDispatcher);
        flora.createInRange(0, (int)windowDimensions.x());
    }

    /**
     * Create the night sky of the game.
     * For it to be effective and realistic, it should be created
     * in front of all other game objects.
     * @param windowDimensions The dimensions of the window.
     */
    private void createNightSky(Vector2 windowDimensions) {
        GameObject nightSky = Night.create(windowDimensions, DAY_NIGHT_CYCLE_SECONDS);
        gameObjects().addGameObject(nightSky, Layer.STATIC_OBJECTS);
    }

    /**
     * Creating all the UI elements of the game.
     * (namely the energy counter, at this stage)
     * @param avatar The avatar of the game.
     */
    private void createUI(Avatar avatar) {
        TextRenderable energyCount = new TextRenderable(GameConstants.ENERGY_COUNTER_TEXT_PREFIX);
        energyCount.setColor(Color.BLACK);
        // Upon updating the character, we update the energy count
        avatar.addComponent((deltaTime -> {
            energyCount.setString(GameConstants.ENERGY_COUNTER_TEXT_PREFIX + avatar.getEnergy());
        }));
        GameObject energyCountObject = new GameObject(
                GameConstants.ENERGY_COUNTER_POS,
                GameConstants.ENERGY_COUNTER_SIZE,
                energyCount);
        gameObjects().addGameObject(energyCountObject);
    }

    /**
     * Handle the collision between a fruit and another object.
     * @param fruit The fruit that was eaten.
     * @param other The object that ate the fruit.
     */
    private void fruitHandler(Consumable fruit, GameObject other) {
        // If the other object is not the avatar, we don't care - Only the avatar can eat the fruit
        if (other != avatar) {
            return;
        }

        avatar.addEnergy(fruit.getEnergy());
        gameObjects().removeGameObject(fruit);
        // Respawn the fruit after a delay - return value is ignored
        new ScheduledTask(
            avatar,
            GameConstants.FRUIT_RESPAWN_DELAY_SECONDS,
            false,
            () -> gameObjects().addGameObject(fruit));
    }

    /**
     * Run the game - A single time, with no repeats or restarts.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

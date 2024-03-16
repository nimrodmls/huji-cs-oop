package pepse.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Dispatcher;
import pepse.util.GameConstants;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.consumables.Consumable;
import pepse.world.consumables.Fruit;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.List;
import java.util.LinkedList;

public class PepseGameManager extends GameManager {

    private static final float AVATAR_CORRECTION = GameConstants.AVATAR_SIZE.x();
    private static final float DAY_NIGHT_CYCLE_SECONDS = 30.0f;
    private static final int TERRAIN_SEED = 0;

    private Dispatcher eventDispatcher;
    private Avatar avatar;
    private Tree tree;

    public PepseGameManager() {

    }

    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader,
            UserInputListener inputListener,
            WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        eventDispatcher = new Dispatcher();

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

    private void createSkyline(Vector2 windowDimensions) {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        GameObject sun = Sun.create(windowDimensions, DAY_NIGHT_CYCLE_SECONDS);
        GameObject sunHalo = SunHalo.create(sun);
        // Allowing the halo to move with the sun
        sunHalo.addComponent((deltaTime -> {
            sunHalo.setCenter(sun.getCenter());
        }));

        // The sun is in front of the halo - order is important
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }

    private Terrain createTerrain(Vector2 windowDimensions) {
        Terrain terrain = new Terrain(windowDimensions, TERRAIN_SEED);
        List<Block> blocks = terrain.createInRange(0, (int)windowDimensions.x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        return terrain;
    }

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

    private void createNightSky(Vector2 windowDimensions) {
        GameObject nightSky = Night.create(windowDimensions, DAY_NIGHT_CYCLE_SECONDS);
        gameObjects().addGameObject(nightSky, Layer.STATIC_OBJECTS);
    }

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

    private void fruitHandler(Consumable fruit, GameObject other) {
        // If the other object is not the avatar, we don't care - Only the avatar can eat the fruit
        if (other != avatar) {
            return;
        }

        avatar.addEnergy(fruit.getEnergy());
        gameObjects().removeGameObject(fruit);
        ScheduledTask fruitRespawn = new ScheduledTask(
                avatar,
                GameConstants.FRUIT_RESPAWN_DELAY_SECONDS,
                false,
                () -> gameObjects().addGameObject(fruit));
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

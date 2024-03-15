package pepse.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.GameConstants;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.List;

public class PepseGameManager extends GameManager {

    private static final float AVATAR_CORRECTION = GameConstants.AVATAR_SIZE.x();
    private static final float DAY_NIGHT_CYCLE_SECONDS = 30.0f;
    private static final int TERRAIN_SEED = 0;

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

        Terrain terrain = new Terrain(windowDimensions, TERRAIN_SEED);
        List<Block> blocks = terrain.createInRange(0, (int)windowDimensions.x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }

        Tree tree = new Tree(
                5,
                new Vector2(60, terrain.groundHeightAt(60)),
                (GameObject obj) -> gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS));

        GameObject nightSky = Night.create(windowDimensions, DAY_NIGHT_CYCLE_SECONDS);
        gameObjects().addGameObject(nightSky, Layer.STATIC_OBJECTS);

        // The initial position of the avatar is at the rightmost corner of the window,
        // just above the ground. The correction is due to the fact that the position given
        // is for the top left corner of the avatar, and we need it above ground & within the window.
        Vector2 avatarInitialPosition = new Vector2(
                windowDimensions.x() - AVATAR_CORRECTION,
                (GameConstants.INITIAL_GROUND_HEIGHT_FACTOR *
                        windowDimensions.y()) - AVATAR_CORRECTION);
        Avatar avatar = new Avatar(avatarInitialPosition, inputListener, imageReader);
        gameObjects().addGameObject(avatar);

        TextRenderable energyCount = new TextRenderable("Energy: N/A");
        energyCount.setColor(Color.BLACK);
        avatar.addComponent((deltaTime -> {
            energyCount.setString("Energy: " + avatar.getEnergy() + "%");
        }));
        GameObject energyCountObject = new GameObject(
                GameConstants.ENERGY_COUNTER_POS,
                GameConstants.ENERGY_COUNTER_SIZE,
                energyCount);
        gameObjects().addGameObject(energyCountObject);

    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

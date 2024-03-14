package pepse.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;

import java.util.List;

public class PepseGameManager extends GameManager {

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

        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        GameObject nightSky = Night.create(windowController.getWindowDimensions(), DAY_NIGHT_CYCLE_SECONDS);
        gameObjects().addGameObject(nightSky, Layer.BACKGROUND);

        GameObject sun = Sun.create(windowController.getWindowDimensions(), DAY_NIGHT_CYCLE_SECONDS);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        Terrain terrain = new Terrain(windowController.getWindowDimensions(), TERRAIN_SEED);
        List<Block> blocks = terrain.createInRange(0, (int)windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

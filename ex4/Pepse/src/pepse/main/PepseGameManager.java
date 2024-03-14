package pepse.main;

import danogl.GameManager;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;

public class PepseGameManager extends GameManager {

    public PepseGameManager() {

    }

    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader,
            UserInputListener inputListener,
            WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        System.out.println("PLACEHOLDER");
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

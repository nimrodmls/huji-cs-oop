package danogl.gui;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for reading Sounds from the disk or from within a jar
 * @author Dan Nirel
 */
public class SoundReader {
    private static final int MAX_SIMULTANEOUS_RUNS = 5;
    private MessageHandler messages;
    private ResourceReader resReader = new ResourceReader();
    private Map<String, Sound> cache = new HashMap<>();

    /** Normally, instances are only created by the library */
    public SoundReader(MessageHandler messages) {
        this.messages = messages;
    }

    /**
     * Read the sound with the specified path from disk or from within the jar
     * @return Sound.EMPTY_SOUND if the sound could not be read
     */
    public Sound readSound(String wavFilePath) {
        var cachedSound = cache.getOrDefault(wavFilePath, null);
        if(cachedSound != null) {
            return cachedSound;
        }

        Clip[] clips = new Clip[MAX_SIMULTANEOUS_RUNS];
        boolean errorReading = false;
        for (int i = 0; i < clips.length ; i++) {
            try (var stream = resReader.readResource(wavFilePath)) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
                clips[i] = AudioSystem.getClip();
                clips[i].open(audioInputStream);
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                messages.showMessage(
                        String.format("Failed to open audio file '%s': %s", wavFilePath, e.getMessage()),
                        MsgLevel.ERROR);
                errorReading = true;
                break;
            }
        }
        var newSound = Sound.EMPTY_SOUND;
        if(!errorReading)
            newSound = new Sound(clips);
        cache.put(wavFilePath, newSound);
        return newSound;
    }

    /**
     * Clear the current cache of loaded sounds. Can be useful in one of the following scenarios:
     * <br>1) Many sounds have been loaded and are using excessive memory, without being used anymore.
     * <br>2) You wish, for some reason,
     * for a particular sound to play SIMULTANEOUSLY many, many times. This is not possible
     * using a single Sound object. Clearing the cache and then loading the sound again would supply
     * additional sound objects.
     * <br>3) The file has changed on-disk since it was last loaded.
     * <br>Note that caching is specific to a SoundReader instance. If you wish to bypass caching
     * altogether, one way of doing so would be creating additional SoundReader instances
     * (each would have its own separate cache).
     */
    public void clearCache() {
        cache.clear();
    }
}

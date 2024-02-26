package danogl.gui;

import javax.sound.sampled.Clip;

/**
 * Represents a sound (sound-effect or music). This class is immutable.
 * @author Dan Nirel
 */
public class Sound {
    /**
     * A Sound instance that plays nothing
     */
    public static final Sound EMPTY_SOUND = new Sound(null);

    private Clip[] clips;

    /** Create a Sound from a clip array. Used internally by the engine */
    public Sound(Clip[] clips) {
        this.clips = clips;
    }

    /** Play the sound once */
    public void play() {
        privatePlay(false);
    }

    /** Play the sound in a loop */
    public void playLooped() {
        privatePlay(true);
    }

    /**
     * Stop playing the sound. If the sound is played multiple times simultaneously,
     * this method stops all occurences
     */
    public void stopAllOccurences() {
        if(this == EMPTY_SOUND)
            return;
        for (var clip : clips)
            clip.stop();
    }

    private void privatePlay(boolean shouldLoop) {
        if(this == EMPTY_SOUND)
            return;

        for (var clip : clips) {
            if(clip.isRunning()) //try next one
                continue;
            clip.setFramePosition(0);
            if(shouldLoop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();
            break;
        }
    }
}

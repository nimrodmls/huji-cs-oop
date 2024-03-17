package pepse.util;

import java.awt.*;

import danogl.util.Vector2;

/**
 * A class containing constants used in the game
 * @author Nimrod M.
 */
public class GameConstants {

    // Animation-related constants

    /**
     * Sequence of paths to the images that make up the idle animation
     */
    public static final String[] IDLE_ANIMATION_PATHS = {
            "assets/idle_0.png",
            "assets/idle_1.png",
            "assets/idle_2.png",
            "assets/idle_3.png"
    };

    /**
     * Sequence of paths to the images that make up the jump animation
     */
    public static final String[] JUMP_ANIMATION_PATHS = {
            "assets/jump_0.png",
            "assets/jump_1.png",
            "assets/jump_2.png",
            "assets/jump_3.png"
    };

    /**
     * Sequence of paths to the images that make up the run animation
     */
    public static final String[] RUN_ANIMATION_PATHS = {
            "assets/run_0.png",
            "assets/run_1.png",
            "assets/run_2.png",
            "assets/run_3.png",
            "assets/run_4.png",
            "assets/run_5.png"
    };

    // World-related constants

    /**
     * The gravity of the world
     */
    public static final float WORLD_GRAVITY = 600;

    /**
     * The height of the ground relative to the window height
     */
    public static final float INITIAL_GROUND_HEIGHT_FACTOR = 2.0f/3.0f;

    // UI-related constants

    /**
     * The title of the window
     */
    public static final String WINDOW_TITLE = "Pepse";

    /**
     * The size of the energy counter UI element
     */
    public static final Vector2 ENERGY_COUNTER_SIZE = new Vector2(100, 20);

    /**
     * The position of the energy counter UI element in the window
     */
    public static final Vector2 ENERGY_COUNTER_POS = new Vector2(50, 50);

    /**
     * The prefix of the energy counter text
     */
    public static final String ENERGY_COUNTER_TEXT_PREFIX = "Energy: ";

    // Avatar-related constants

    /**
     * The size of the avatar
     */
    public static final Vector2 AVATAR_SIZE = new Vector2(50, 50);

    /**
     * The maximum energy of the avatar
     */
    public static final float AVATAR_MAX_ENERGY = 100.0f;

    /**
     * The energy cost of moving the avatar (run)
     */
    public static final float AVATAR_RUN_ENERGY_COST = 0.5f;

    /**
     * The energy cost of jumping with the avatar
     */
    public static final float AVATAR_JUMP_ENERGY_COST = 10.0f;

    /**
     * The name of the event triggerred when the avatar jumps
     */
    public static final String AVATAR_JUMP_EVENT = "avatarJump";

    // Flora-related constants

    /**
     * The default fruit color
     */
    public static final Color DEFAULT_FRUIT_COLOR = Color.red;

    /**
     * The alternative fruit color
     */
    public static final Color ALT_FRUIT_COLOR = Color.orange;

    /**
     * The delay between eating a fruit and its respawn
     */
    public static final float FRUIT_RESPAWN_DELAY_SECONDS = 30.f;
}

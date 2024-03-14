package pepse.util;

import danogl.util.Vector2;

public class GameConstants {
    public static final String[] IDLE_ANIMATION_PATHS = {
            "assets/idle_0.png",
            "assets/idle_1.png",
            "assets/idle_2.png",
            "assets/idle_3.png"
    };
    public static final String[] JUMP_ANIMATION_PATHS = {
            "assets/jump_0.png",
            "assets/jump_1.png",
            "assets/jump_2.png",
            "assets/jump_3.png"
    };
    public static final String[] RUN_ANIMATION_PATHS = {
            "assets/run_0.png",
            "assets/run_1.png",
            "assets/run_2.png",
            "assets/run_3.png",
            "assets/run_4.png",
            "assets/run_5.png"
    };
    public static final String IDLE0_AVATAR_IMG_PATH = "assets/idle_0.png";
    public static final String IDLE1_AVATAR_IMG_PATH = "assets/idle_1.png";
    public static final String IDLE2_AVATAR_IMG_PATH = "assets/idle_2.png";
    public static final String IDLE3_AVATAR_IMG_PATH = "assets/idle_3.png";
    public static final String JUMP0_AVATAR_IMG_PATH = "assets/jump_0.png";
    public static final String JUMP1_AVATAR_IMG_PATH = "assets/jump_1.png";
    public static final String JUMP2_AVATAR_IMG_PATH = "assets/jump_2.png";
    public static final String JUMP3_AVATAR_IMG_PATH = "assets/jump_3.png";
    public static final String RUN0_AVATAR_IMG_PATH = "assets/run_0.png";
    public static final String RUN1_AVATAR_IMG_PATH = "assets/run_1.png";
    public static final String RUN2_AVATAR_IMG_PATH = "assets/run_2.png";
    public static final String RUN3_AVATAR_IMG_PATH = "assets/run_3.png";
    public static final String RUN4_AVATAR_IMG_PATH = "assets/run_4.png";
    public static final String RUN5_AVATAR_IMG_PATH = "assets/run_5.png";

    public static final float INITIAL_GROUND_HEIGHT_FACTOR = 2.0f/3.0f;
    public static final Vector2 ENERGY_COUNTER_SIZE = new Vector2(100, 20);
    public static final Vector2 ENERGY_COUNTER_POS = new Vector2(50, 50);

    public static final float GRAVITY = 600;
    public static final Vector2 AVATAR_SIZE = new Vector2(50, 50);
    public static final float AVATAR_MAX_ENERGY = 100.0f;
    public static final float AVATAR_MOVE_ENERGY_COST = 0.5f;
    public static final float AVATAR_JUMP_ENERGY_COST = 10.0f;
}

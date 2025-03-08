package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.some_example_name.screen.MainMenuScreen;

public class BouncingBallGame extends Game {
    public static final float WORLD_WIDTH = 1280;
    public static final float WORLD_HEIGHT = 720;

    public SpriteBatch batch;
    public FitViewport viewport;


    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);

        this.setScreen(new MainMenuScreen(this));
    }
}

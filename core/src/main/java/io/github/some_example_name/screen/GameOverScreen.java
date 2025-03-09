package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.some_example_name.BouncingBallGame;

public class GameOverScreen extends ScreenAdapter {
    private final BouncingBallGame game;

    private final Texture gameOverTexture;
    private final Sprite gameOverSprite;
    private final Viewport viewport;

    public GameOverScreen(BouncingBallGame game) {
        this.game = game;

        viewport = game.viewport;
        gameOverTexture = new Texture("gameOver.png");
        gameOverSprite = new Sprite(gameOverTexture);
        gameOverSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        viewport.apply();

        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.batch.begin();
        gameOverSprite.draw(game.batch);
        game.batch.end();
        restart();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void restart() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void dispose() {
        gameOverTexture.dispose();
    }
}

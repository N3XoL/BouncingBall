package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.BouncingBallGame;

public class MainMenuScreen extends ScreenAdapter {
    private final  BouncingBallGame game;
    private final Texture background;
    private final Texture playButton;
    private final Sprite backgroundSprite;
    private final Sprite playButtonSprite;
    private final Vector2 touchPos;
    private final Rectangle bounds;

    public MainMenuScreen(BouncingBallGame game) {
        this.game = game;

        touchPos = new Vector2();
        background = new Texture("mainMenu.png");
        playButton = new Texture("playButton.jpg");
        backgroundSprite = new Sprite(background);
        playButtonSprite = new Sprite(playButton);
        backgroundSprite.setSize(1280, 720);
        playButtonSprite.setSize(200, 100);
        playButtonSprite.setX(BouncingBallGame.WORLD_WIDTH / 2 - playButtonSprite.getWidth() / 2);
        playButtonSprite.setY(10);
        bounds = new Rectangle(playButtonSprite.getX(), playButtonSprite.getY(), playButtonSprite.getWidth(), playButtonSprite.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        backgroundSprite.draw(game.batch);
        playButtonSprite.draw(game.batch);
        game.batch.end();
        clickPlay();
    }

    private void clickPlay() {
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);
            if (bounds.contains(touchPos)) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
    }
}

package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.some_example_name.BouncingBallGame;
import io.github.some_example_name.model.Ball;
import io.github.some_example_name.model.Block;
import io.github.some_example_name.model.Paddle;
import io.github.some_example_name.model.ShootingUpgrade;

import static io.github.some_example_name.BouncingBallGame.WORLD_HEIGHT;
import static io.github.some_example_name.BouncingBallGame.WORLD_WIDTH;

public class GameScreen extends ScreenAdapter {
    private final static float PADDLE_WIDTH = 100f;
    private final static float PADDLE_HEIGHT = 20f;

    private final static float BALL_RADIUS = 8f;
    private final static float BALL_X_SPEED = 400f;
    private final static float BALL_Y_SPEED = 400f;

    private final static float UPGRADE_X_SPEED = 400f;
    private final static float UPGRADE_Y_SPEED = 400f;

    private final static float BLOCK_WIDTH = 81f;
    private final static float BLOCK_HEIGHT = 23f;
    private final static float BLOCK_SPACE_BETWEEN = 5f;

    private final BouncingBallGame game;
    private final SpriteBatch batch;

    private final Texture backgroundTexture;
    private final Sprite backgroundSprite;

    private final Ball ball;
    private final Paddle paddle;
    private final Array<Block> blocks;
    private final  ShapeRenderer shapeRenderer;
    private final Viewport viewport;
    private final ShootingUpgrade shootingUpgrade;

    boolean ballMoving = false;

    public GameScreen(BouncingBallGame game) {
        this.game = game;

        viewport = game.viewport;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        blocks = new Array<>();
        paddle = new Paddle(WORLD_WIDTH / 2 - PADDLE_WIDTH / 2, 10, PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Ball(
            WORLD_WIDTH / 2,
            paddle.getBounds().y + paddle.getBounds().height + BALL_RADIUS,
            BALL_RADIUS,
            BALL_X_SPEED, BALL_Y_SPEED
        );
        shootingUpgrade = new ShootingUpgrade(UPGRADE_X_SPEED, UPGRADE_Y_SPEED);

        backgroundTexture = new Texture("background.png");
        backgroundSprite = new Sprite(backgroundTexture);

        backgroundSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        for (float y = WORLD_HEIGHT / 2; y < WORLD_HEIGHT; y += BLOCK_HEIGHT + BLOCK_SPACE_BETWEEN) {
            for (float x = 0; x < WORLD_WIDTH; x += BLOCK_WIDTH + BLOCK_SPACE_BETWEEN) {
                blocks.add(new Block(x, y, BLOCK_WIDTH, BLOCK_HEIGHT));
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        backgroundSprite.draw(batch);
        ball.draw(batch);
        paddle.draw(batch);
        for (Block block : blocks) {
            block.draw(batch);
        }
        shootingUpgrade.drawIcon(batch);

        batch.end();

        if (shootingUpgrade.isActive()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shootingUpgrade.drawProjectiles(shapeRenderer);
            shapeRenderer.end();
        }

        update();
    }

    public void update() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            ballMoving = true;
        }

        if (ballMoving) {
            ball.update(Gdx.graphics.getDeltaTime());
            paddle.update(viewport);
            paddle.checkCollision(ball);
            if (ball.getBounds().y - ball.getBounds().radius <= 0) {
                game.setScreen(new GameOverScreen(game));
            }

            for (Block block : blocks) {
                if (block.checkCollision(ball) || shootingUpgrade.checkCollisions(block)) {
                    ball.speedVelocity(1.005f);
                    blocks.removeValue(block, true);
                    if (Math.random() < .1f && !shootingUpgrade.isActive() && !shootingUpgrade.isIconFalling()) {
                        shootingUpgrade.activateIcon(block.getBounds().x, block.getBounds().y);
                    }
                }
            }

            shootingUpgrade.updateIcon(Gdx.graphics.getDeltaTime(), paddle);
            shootingUpgrade.updateProjectiles(Gdx.graphics.getDeltaTime(), paddle);

            if (blocks.isEmpty()) {
                game.setScreen(new WinningScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        backgroundTexture.dispose();
        ball.getTexture().dispose();
        paddle.getTexture().dispose();
        shootingUpgrade.getTexture().dispose();
        blocks.forEach(b -> b.getTexture().dispose());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}

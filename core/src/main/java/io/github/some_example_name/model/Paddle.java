package io.github.some_example_name.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Paddle {
    private final Rectangle bounds;
    private final Vector2 touchPos;

    private final Texture texture;
    private final Sprite sprite;

    public Paddle(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
        touchPos = new Vector2();

        texture = new Texture("paddle.png");
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(bounds.x, bounds.y);
        sprite.draw(batch);
    }

    public void update(Viewport viewport) {
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);

            bounds.x = touchPos.x - bounds.width / 2;
            bounds.x = MathUtils.clamp(bounds.x, 0, viewport.getWorldWidth() - bounds.width);
        }
    }

    public void checkCollision(Ball ball) {
        if (Intersector.overlaps(ball.getBounds(), bounds)) {
            Vector2 velocity = ball.getVelocity();
            velocity.y = -velocity.y;

            float paddleCenter = bounds.x + bounds.width / 2;
            float hitPosition = ball.getBounds().x - paddleCenter;
            float normalizedVelocity = hitPosition / (bounds.width / 2);
            float bounceAngle = normalizedVelocity * MathUtils.PI / 3;
            float speed = velocity.len();
            velocity.x = speed * MathUtils.sin(bounceAngle);
            velocity.y = Math.abs(speed * MathUtils.cos(bounceAngle));
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

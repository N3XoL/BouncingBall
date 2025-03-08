package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.BouncingBallGame;

public class Ball {
    private final Circle bounds;
    private final Vector2 velocity;

    Texture texture;
    Sprite sprite;

    public Ball(float x, float y, float radius, float xSpeed, float ySpeed) {
        this.bounds = new Circle(x, y, radius);
        this.velocity = new Vector2(xSpeed, ySpeed);

        texture = new Texture("ball.png");
        sprite = new Sprite(texture);
        sprite.setSize(2 * radius, 2 * radius);
    }

    public void update(float delta) {
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        float minX = bounds.radius;
        float maxX = BouncingBallGame.WORLD_WIDTH - bounds.radius;
        if (bounds.x < minX || bounds.x > maxX) {
            bounds.x = MathUtils.clamp(bounds.x, minX, maxX);
            velocity.x = -velocity.x;
        }

        float minY = bounds.radius;
        float maxY = BouncingBallGame.WORLD_HEIGHT - bounds.radius;
        if (bounds.y < minY || bounds.y > maxY) {
            bounds.y = MathUtils.clamp(bounds.y, minY, maxY);
            velocity.y = -velocity.y;
        }
    }

    public void speedVelocity(float delta) {
        velocity.scl(delta);
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(bounds.x - bounds.radius, bounds.y - bounds.radius);
        sprite.draw(batch);
    }

    public Circle getBounds() {
        return bounds;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}

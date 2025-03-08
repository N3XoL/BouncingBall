package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Block {
    private final Rectangle bounds;

    Texture texture;
    Sprite textureSprite;

    public Block(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);

        texture = new Texture("block.png");
        textureSprite = new Sprite(texture);
        textureSprite.setSize(width, height);
    }

    public void draw(SpriteBatch batch) {
        textureSprite.setPosition(bounds.x, bounds.y);
        textureSprite.draw(batch);
    }

    public boolean checkCollision(Ball ball) {
        if (Intersector.overlaps(ball.getBounds(), bounds)) {
            float leftEdge = bounds.x;
            float rightEdge = bounds.x + bounds.width;
            float topEdge = bounds.y + bounds.height;
            float bottomEdge = bounds.y;

            if (ball.getBounds().y + ball.getBounds().radius >= bottomEdge
                && ball.getBounds().x >= leftEdge && ball.getBounds().x <= rightEdge) {
                ball.getVelocity().y = -ball.getVelocity().y;
            } else if (ball.getBounds().y - ball.getBounds().radius <= topEdge
                && ball.getBounds().x >= leftEdge && ball.getBounds().x <= rightEdge) {
                ball.getVelocity().y = -ball.getVelocity().y;
            } else if (ball.getBounds().x + ball.getBounds().radius >= leftEdge
                && ball.getBounds().y <= topEdge && ball.getBounds().y >= bottomEdge) {
                ball.getVelocity().x = -ball.getVelocity().x;
            } else if (ball.getBounds().x - ball.getBounds().radius <= rightEdge
                && ball.getBounds().y <= topEdge && ball.getBounds().y >= bottomEdge) {
                ball.getVelocity().x = -ball.getVelocity().x;
            } else {
                ball.getVelocity().x = -ball.getVelocity().x;
                ball.getVelocity().y = -ball.getVelocity().y;
            }
            return true;
        }
        return false;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

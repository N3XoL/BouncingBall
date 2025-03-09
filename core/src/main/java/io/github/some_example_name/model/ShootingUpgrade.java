package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.BouncingBallGame;

public class ShootingUpgrade {
    private final static float ICON_WIDTH = 10;
    private final static float ICON_HEIGHT = 30;

    private final static float PROJECTILE_RADIUS = 5;

    private final Array<Circle> projectiles;
    private final Vector2 velocity;

    private final Texture texture;
    private final Sprite textureSprite;

    float spawnTimer;
    boolean isIconFalling = false;
    boolean isActive = false;
    float xSpeed, ySpeed;
    long startTime;
    long upgradeDuration = 10_000;

    public ShootingUpgrade(float xSpeed, float ySpeed) {
        this.projectiles = new Array<>();
        this.velocity = new Vector2(xSpeed, ySpeed);

        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;

        texture = new Texture("shootingUpgrade.png");
        textureSprite = new Sprite(texture);

        textureSprite.setSize(ICON_WIDTH, ICON_HEIGHT);
    }

    private void addProjectile(float x, float y, float spaceBetween) {
        projectiles.add(new Circle(x + PROJECTILE_RADIUS, y + PROJECTILE_RADIUS, PROJECTILE_RADIUS));
        projectiles.add(new Circle(x + (spaceBetween - PROJECTILE_RADIUS), y + PROJECTILE_RADIUS, PROJECTILE_RADIUS));
    }

    public void drawIcon(SpriteBatch batch) {
        if (isIconFalling) {
            textureSprite.draw(batch);
        }
    }

    public void drawProjectiles(ShapeRenderer shapeRenderer) {
        for (Circle projectile : projectiles) {
            shapeRenderer.circle(projectile.x, projectile.y, projectile.radius);
        }
    }

    public void activateIcon(float x, float y) {
        textureSprite.setPosition(x, y);
        setIconFalling(true);
    }

    public void setIconFalling(boolean status) {
        isIconFalling = status;
    }

    public boolean isIconFalling() {
        return isIconFalling;
    }

    public void updateIcon(float delta, Paddle paddle) {
        if (isIconFalling) {
            textureSprite.translateY(-velocity.y * delta);
            checkIconCollision(paddle);
        }
    }

    public void updateProjectiles(float delta, Paddle paddle) {
        if (isActive) {
            if (System.currentTimeMillis() - startTime >= upgradeDuration) {
                projectiles.clear();
                isActive = false;
                spawnTimer = 0;
                textureSprite.setAlpha(100);
            }

            for (Circle projectile : projectiles) {
                projectile.y += velocity.y * delta;
            }

            for (int i = projectiles.size - 1; i >= 0; i--) {
                Circle projectile = projectiles.get(i);
                if (projectile.y > BouncingBallGame.WORLD_HEIGHT) {
                    projectiles.removeIndex(i);
                }
            }

            spawnTimer += delta;
            if (spawnTimer > 0.5f) {
                addProjectile(
                    paddle.getBounds().x,
                    paddle.getBounds().y + paddle.getBounds().height,
                    paddle.getBounds().width
                );
                spawnTimer = 0;
            }
        }
    }

    public boolean checkCollisions(Block block) {
        for (Circle projectile : projectiles) {
            if (Intersector.overlaps(projectile, block.getBounds())) {
                projectiles.removeValue(projectile, true);
                return true;
            }
        }
        return false;
    }

    public boolean isActive() {
        return isActive;
    }

    private void checkIconCollision(Paddle paddle) {
        if (paddle.getBounds().contains(textureSprite.getX(), textureSprite.getY())) {
            startTime = System.currentTimeMillis();
            isActive = true;
            isIconFalling = false;
            textureSprite.setAlpha(0);
            addProjectile(
                paddle.getBounds().x,
                paddle.getBounds().y + paddle.getBounds().height,
                paddle.getBounds().width
            );
        } else if (textureSprite.getY() + textureSprite.getHeight() <= 0) {
            isIconFalling = false;
        }
    }

    public Texture getTexture() {
        return texture;
    }
}

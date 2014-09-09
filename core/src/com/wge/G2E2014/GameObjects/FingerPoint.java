package com.wge.G2E2014.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;

import static com.wge.G2E2014.Helpers.Box2DHelper.PixelsToMeters;
import static com.wge.G2E2014.Helpers.Box2DHelper.MetersToPixels;

public class FingerPoint {
    private ShapeRenderer shapeRenderer;
    private float xPos;
    private float yPos;
    private float xPosPrev;
    private float yPosPrev;
    private float radius;
    private int pointerIndex;

    private World parentWorld;
    private Body body;

    private Texture ballTexture;
    private Texture shineTexture;
    private Texture logoTexture;

    private Texture stoneBaseTexture;
    private Texture stonePatternTexture;
    private Texture stoneShineTexture;
    private Texture stoneCapTexture;
    private Texture stoneCapShineTexture;
    private Texture stoneHandleTexture;
    private Texture stoneHandleShineTexture;

    private final Color colors[] = {Color.LIGHT_GRAY, Color.BLUE, Color.RED, Color.MAGENTA, Color.YELLOW,
            Color.DARK_GRAY, Color.PINK, Color.PURPLE, Color.CYAN, Color.GREEN};

    public FingerPoint(float x, float y, int pointer, World world) {
        shapeRenderer = new ShapeRenderer();
        xPos = xPosPrev = x;
        yPos = yPosPrev = y;
        pointerIndex = pointer;
        radius = 64.0f;
        parentWorld = world;

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();

        bDef.position.set(PixelsToMeters(xPos), PixelsToMeters(yPos));
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = parentWorld.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(PixelsToMeters(radius));
        fDef.shape = shape;
        fDef.restitution = 0.9f;
        fDef.friction = 0.8f;
        fDef.density = 500;
        body.setAngularDamping(0.3f);
        body.setLinearDamping(0.9f);
        body.createFixture(fDef);
        body.resetMassData();

        //add reference to this finger point object to the body, for circular reference
        body.setUserData(this);

        ballTexture = new Texture(Gdx.files.internal("ball_grayscale.png"));
        shineTexture = new Texture(Gdx.files.internal("ball_shine.png"));
        logoTexture = new Texture(Gdx.files.internal("ball_logo.png"));

        stoneBaseTexture = new Texture(Gdx.files.internal("stone.png"));
        stonePatternTexture = new Texture(Gdx.files.internal("stone_pattern.png"));
        stoneShineTexture = new Texture(Gdx.files.internal("stone_shine.png"));
        stoneCapTexture = new Texture(Gdx.files.internal("stone_cap_grayscale.png"));
        stoneCapShineTexture = new Texture(Gdx.files.internal("stone_cap_shine.png"));
        stoneHandleTexture = new Texture(Gdx.files.internal("stone_handle_grayscale.png"));
        stoneHandleShineTexture = new Texture(Gdx.files.internal("stone_handle_shine.png"));
    }

    public void draw(Batch batch, boolean curlingMode) {
        //update finger point sprite based on physics position before drawing
        xPos = MetersToPixels(body.getPosition().x);
        yPos = MetersToPixels(body.getPosition().y);

        float rotation = body.getAngle() * MathUtils.radiansToDegrees;

        if(curlingMode) {
            batch.draw(stoneBaseTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, 0f, 0, 0, 256, 256, false, false);
            batch.draw(stonePatternTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, rotation, 0, 0, 256, 256, false, false);
            batch.draw(stoneShineTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, 0f, 0, 0, 256, 256, false, false);
            batch.setColor(colors[pointerIndex]);
            batch.draw(stoneCapTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, rotation, 0, 0, 256, 256, false, false);
            batch.setColor(Color.WHITE);
            batch.draw(stoneCapShineTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, 0f, 0, 0, 256, 256, false, false);
            batch.setColor(colors[pointerIndex]);
            batch.draw(stoneHandleTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, rotation, 0, 0, 256, 256, false, false);
            batch.setColor(Color.WHITE);
            batch.draw(stoneHandleShineTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, rotation, 0, 0, 256, 256, false, false);
        }
        else {
            batch.setColor(colors[pointerIndex]);
            batch.draw(ballTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, 0f, 0, 0, 256, 256, false, false);
            batch.setColor(Color.WHITE);
            batch.draw(logoTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, rotation, 0, 0, 256, 256, false, false);
            batch.draw(shineTexture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, 0f, 0, 0, 256, 256, false, false);
        }
    }

    public void setPos(float x, float y, float delta) {
        xPosPrev = xPos;
        yPosPrev = yPos;

        xPos = x;
        yPos = y;

        body.setTransform(PixelsToMeters(xPos), PixelsToMeters(yPos), 0f);
        body.setLinearVelocity(PixelsToMeters(xPos - xPosPrev)/delta, PixelsToMeters(yPos - yPosPrev)/delta);
    }

    public void freeze() {
        body.setActive(false);
        body.setLinearVelocity(0f,0f);
        body.setAngularVelocity(0f);
    }

    public void unFreeze() {
        body.setActive(true);
        body.setAwake(true);
    }

    public void dispose() {
        parentWorld.destroyBody(body);
    }
}

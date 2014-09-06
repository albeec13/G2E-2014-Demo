package com.wge.G2E2014.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

    private Texture texture;

    private final Color colors[] = {Color.WHITE, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW,
            Color.ORANGE, Color.PINK, Color.PURPLE, Color.CYAN, Color.OLIVE};

    public FingerPoint(float x, float y, int pointer, World world) {
        shapeRenderer = new ShapeRenderer();
        xPos = xPosPrev = x;
        yPos = yPosPrev = y;
        pointerIndex = pointer;
        radius = 50.0f;
        parentWorld = world;

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();

        bDef.position.set(PixelsToMeters(xPos), PixelsToMeters(yPos));
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = parentWorld.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(PixelsToMeters(radius));
        fDef.shape = shape;
        fDef.restitution = 0.8f;
        fDef.friction = 0.1f;
        fDef.density = 1;
        body.createFixture(fDef);

        //add reference to this finger point object to the body, for circular reference
        body.setUserData(this);

        texture = new Texture(Gdx.files.internal("Pokeball.png"));
    }

    public void draw(Batch batch) {
        batch.end();

        //update finger point sprite based on physics position before drawing
        xPos = MetersToPixels(body.getPosition().x);
        yPos = MetersToPixels(body.getPosition().y);

        /*Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(colors[pointerIndex]);
        shapeRenderer.circle(this.xPos, this.yPos, this.radius, 100);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);*/

        batch.begin();

        float rotation = body.getAngle() * MathUtils.radiansToDegrees;
        batch.draw(texture, xPos - radius, yPos - radius, radius, radius, radius * 2, radius * 2, 1, 1, rotation, 0, 0, 256, 256,false, false);
        //batch.draw(texture, body.getPosition().x, body.getPosition().y, xPos, yPos, radius * 2, radius * 2, 1, 1, rotation, 0, 0, 256, 256,false, false);
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
}

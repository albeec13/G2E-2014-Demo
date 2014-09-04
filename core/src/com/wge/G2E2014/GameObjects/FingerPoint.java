package com.wge.G2E2014.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.wge.G2E2014.Helpers.Box2DHelper.PPM;

/**
 * Created by achaharbakhshi on 9/4/2014.
 */
public class FingerPoint {
    private ShapeRenderer shapeRenderer;
    private float xPos;
    private float yPos;
    private float radius;
    private int pointerIndex;

    private World parentWorld;
    private Body body;

    private final Color colors[] = {Color.WHITE, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW,
            Color.ORANGE, Color.PINK, Color.PURPLE, Color.CYAN, Color.OLIVE};

    public FingerPoint(float x, float y, int pointer, World world) {
        shapeRenderer = new ShapeRenderer();
        xPos = x;
        yPos = y;
        pointerIndex = pointer;
        radius = 50.0f;

        parentWorld = world;

        BodyDef bDef;
        FixtureDef fDef;

        bDef = new BodyDef();
        bDef.position.set(PPM(xPos), PPM(yPos));
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = parentWorld.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(PPM(radius));
        fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.restitution = 0.5f;
        body.createFixture(fDef);
    }

    public void draw(Batch batch) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(colors[pointerIndex]);
        shapeRenderer.circle(this.xPos, this.yPos, this.radius, 100);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    public void setPos(float x, float y) {
        xPos = x;
        yPos = y;

        body.setTransform(PPM(xPos), PPM(yPos), body.getAngle());
    }

    public int getPointer() {
        return pointerIndex;
    }
}

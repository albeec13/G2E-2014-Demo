package com.wge.G2E2014.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;

import static com.wge.G2E2014.Helpers.Box2DHelper.PixelsToMeters;

/**
 * Created by achaharbakhshi on 9/11/2014.
 */
public class Peg {
    private float xPos;
    private float yPos;
    private World parentWorld;
    private float radius;
    private Body body;
    private boolean hidden = false;
    private ShapeRenderer shapeRenderer;

    public Peg(float x, float y, World world, boolean StartHidden) {
        xPos = x;
        yPos = y;
        parentWorld = world;
        radius = 20f;

        shapeRenderer = new ShapeRenderer();

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        CircleShape pegShape = new CircleShape();

        bDef.position.set(PixelsToMeters(x), PixelsToMeters(y));
        bDef.type = BodyDef.BodyType.StaticBody;
        pegShape.setRadius(PixelsToMeters(radius));
        fDef.shape = pegShape;

        body = parentWorld.createBody(bDef);
        body.setType(BodyDef.BodyType.StaticBody);
        body.createFixture(fDef);
        body.setUserData(this);

        this.hide(StartHidden);
    }

    public void draw(Batch batch) {
        if(!hidden) {
            batch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.circle(xPos,yPos,radius,50);
            shapeRenderer.end();

            batch.begin();
        }
    }

    public void hide(boolean hide) {
        if(hide) {
            body.setActive(false);
            hidden = true;
        }
        else {
            body.setActive(true);
            hidden = false;
        }

    }

    public void dispose() {
        parentWorld.destroyBody(body);
        shapeRenderer.dispose();
    }
}

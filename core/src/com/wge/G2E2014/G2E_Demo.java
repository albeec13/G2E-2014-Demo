package com.wge.G2E2014;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import static com.wge.G2E2014.Helpers.Box2DHelper.PixelsToMeters;

import com.badlogic.gdx.utils.Array;
import com.wge.G2E2014.GameObjects.*;

import java.util.HashMap;
import java.util.Iterator;

public class G2E_Demo implements ApplicationListener, InputProcessor {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera b2dCamera;
    private HashMap<Integer, FingerPoint> FingerPoints;
    private boolean gravityOn = false;
    private Vector3 touchVector;
    private float accumulator = 0.0f;
    private static float physicsDelta = 1/45f;
    private BitmapFont font;
    private int fingersOnScreen = 0;

    @Override
    public void create () {
        Gdx.input.setInputProcessor(this);

        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();

        //set game camera, spritebatch, and font
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setScale(2,2);

        //create box2d world, renderer, camera, and bounding box
        world = new World(new Vector2(0, 0), true);
        gravityOn = false;
        debugRenderer = new Box2DDebugRenderer();
        b2dCamera = new OrthographicCamera(PixelsToMeters(width), PixelsToMeters(height));
        b2dCamera.setToOrtho(false, PixelsToMeters(width), PixelsToMeters(height));

        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.position.set(PixelsToMeters(0), PixelsToMeters(0));
        wallBodyDef.type = BodyDef.BodyType.StaticBody;
        Body wallBody = world.createBody(wallBodyDef);

        EdgeShape wallShape = new EdgeShape();
        FixtureDef wallFixtureDef = new FixtureDef();
        wallFixtureDef.shape = wallShape;

        //left wall
        wallShape.set(new Vector2(PixelsToMeters(0), PixelsToMeters(0)), new Vector2(PixelsToMeters(0), PixelsToMeters(height)));
        wallBody.createFixture(wallFixtureDef);

        //top wall
        wallShape.set(new Vector2(PixelsToMeters(0), PixelsToMeters(height)), new Vector2(PixelsToMeters(width), PixelsToMeters(height)));
        wallBody.createFixture(wallFixtureDef);

        //right wall
        wallShape.set(new Vector2(PixelsToMeters(width), PixelsToMeters(height)), new Vector2(PixelsToMeters(width), PixelsToMeters(0)));
        wallBody.createFixture(wallFixtureDef);

        //bottom wall
        wallShape.set(new Vector2(PixelsToMeters(width), PixelsToMeters(0)), new Vector2(PixelsToMeters(0), PixelsToMeters(0)));
        wallBody.createFixture(wallFixtureDef);

        //create touch point tracking hash map and vector for tracking incoming touches
        touchVector = new Vector3();
        FingerPoints = new HashMap<Integer, FingerPoint>();
    }

    @Override
    public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        b2dCamera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for(int i = 0; i < 10; i++) {
            if(FingerPoints.containsKey(i)) {
                FingerPoints.get(i).draw(batch);
            }
        }

        //font.draw(batch, "Fingers: " + fingersOnScreen, 20, Gdx.graphics.getHeight() - 20);
        //font.draw(batch, "Gravity Enabled: " + gravityOn, Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 20);

        font.draw(batch, "Fingers: " + fingersOnScreen, 20, Gdx.graphics.getHeight()/2);
        font.draw(batch, "Gravity Enabled: " + gravityOn, Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight()/2);
        batch.end();

        //update (and render debug for) box2d physics
        //debugRenderer.render(world, b2dCamera.combined);
        accumulator += deltaTime;
        while(accumulator >= physicsDelta) {
            world.step(physicsDelta, 6, 2);
            accumulator -= physicsDelta;
        }
        //world.step(physicsDelta, 6, 2);
    }

    @Override
    public void resize (int width, int height) {

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void dispose () {
        batch.dispose();
        font.dispose();
        FingerPoints.clear();
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.BACK:
                Gdx.app.exit();
                return true;
            case Input.Keys.A:
                //FingerPoints.clear();
                toggleGravity();
                return true;
            case Input.Keys.MENU:
                toggleGravity();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 10) {
            fingersOnScreen++;

            updateFingerPoints(screenX, screenY, pointer);

            //freeze physics on finger down
            FingerPoints.get(pointer).freeze();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 10) {
            fingersOnScreen--;

            //unfreeze physics on finger up
            FingerPoints.get(pointer).unFreeze();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer < 10) {
            updateFingerPoints(screenX, screenY, pointer);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void updateFingerPoints(int x, int y, int pointer) {
        //convert from touch coordinate system to camera/game coordinate system
        touchVector.set(x, y, 0);
        camera.unproject(touchVector);

        if(FingerPoints.containsKey(pointer)) {
            FingerPoints.get(pointer).setPos(touchVector.x, touchVector.y, Gdx.graphics.getDeltaTime());
        }
        else {
            FingerPoints.put(pointer, new FingerPoint(touchVector.x, touchVector.y, pointer, world));
            FingerPoints.get(pointer).setPos(touchVector.x, touchVector.y, Gdx.graphics.getDeltaTime());
        }
    }

    private void toggleGravity() {
        if(gravityOn) {
            world.setGravity(new Vector2(0f, 0f));
            gravityOn = false;
        }
        else {
            world.setGravity(new Vector2(0f, -9.8f));
            gravityOn = true;

            Array<Body> worldBodies = new Array<Body>();
            world.getBodies(worldBodies);

            for(Body b : worldBodies) {
                b.setAwake(true);
            }
        }
    }
}

package com.wge.G2E2014;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import static com.wge.G2E2014.Helpers.Box2DHelper.*;
import com.wge.G2E2014.GameObjects.*;

import java.util.HashMap;

public class G2E_Demo implements ApplicationListener, InputProcessor {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera b2dCamera;
    private HashMap<Integer, FingerPoint> FingerPoints;

    private Vector3 touchVector;

    @Override
    public void create () {
        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);

        FingerPoints = new HashMap<Integer, FingerPoint>();

        debugRenderer = new Box2DDebugRenderer();
        b2dCamera = new OrthographicCamera(PPM(Gdx.graphics.getWidth()), PPM(Gdx.graphics.getHeight()));
        b2dCamera.setToOrtho(false, PPM(Gdx.graphics.getWidth()), PPM(Gdx.graphics.getHeight()));

        touchVector = new Vector3();
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
        batch.end();

        debugRenderer.render(world, b2dCamera.combined);
        world.step(1/45f, 6, 2);
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
        FingerPoints.clear();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.BACK:
                Gdx.app.exit();
                return true;
            case Input.Keys.A:
                FingerPoints.clear();
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
            System.out.println(pointer);

            touchVector.set(screenX, screenY, 0);
            camera.unproject(touchVector);

            if(FingerPoints.containsKey(pointer)) {
                FingerPoints.get(pointer).setPos(touchVector.x, touchVector.y);
            }
            else {
                FingerPoints.put(pointer, new FingerPoint(touchVector.x, touchVector.y, pointer, world));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer < 10) {
            touchVector.set(screenX, screenY, 0);
            camera.unproject(touchVector);

            if(FingerPoints.containsKey(pointer)) {
                FingerPoints.get(pointer).setPos(touchVector.x, touchVector.y);
            }
            else {
                FingerPoints.put(pointer, new FingerPoint(touchVector.x, touchVector.y, pointer, world));
            }
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
}

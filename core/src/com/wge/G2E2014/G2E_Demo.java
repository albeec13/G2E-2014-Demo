package com.wge.G2E2014;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;
import java.util.Iterator;

public class G2E_Demo implements ApplicationListener, InputProcessor {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private HashMap<Integer, FingerPoint> FingerPoints;

    public class FingerPoint {
        private ShapeRenderer shapeRenderer;
        private float xPos;
        private float yPos;
        private float radius;
        private int pointerIndex;

        private final Color colors[] = {Color.WHITE, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW,
                                            Color.ORANGE, Color.PINK, Color.PURPLE, Color.CYAN, Color.OLIVE};

        public FingerPoint(float x, float y, int pointer) {
            shapeRenderer = new ShapeRenderer();
            xPos = x;
            yPos = y;
            pointerIndex = pointer;
            radius = 20.0f;
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

        public void setPos(int screenX, int screenY) {
            xPos = screenX;
            yPos = screenY;
        }

        public int getPointer() {
            return pointerIndex;
        }
    }

    @Override
    public void create () {
        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();

        FingerPoints = new HashMap<Integer, FingerPoint>();
    }

    @Override
    public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for(int i = 0; i < 10; i++) {
            if(FingerPoints.containsKey(i)) {
                FingerPoints.get(i).draw(batch);
            }
        }
        batch.end();
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
            if(FingerPoints.containsKey(pointer)) {
                FingerPoints.get(pointer).setPos(screenX, screenY);
            }
            else {
                FingerPoints.put(pointer, new FingerPoint(screenX, screenY, pointer));
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
            if(FingerPoints.containsKey(pointer)) {
                FingerPoints.get(pointer).setPos(screenX, screenY);
            }
            else {
                FingerPoints.put(pointer, new FingerPoint(screenX, screenY, pointer));
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

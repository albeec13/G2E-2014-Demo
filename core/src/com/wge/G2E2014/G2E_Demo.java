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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Iterator;

public class G2E_Demo implements ApplicationListener, InputProcessor {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Array<FingerPoint> FingerPoints;

    public class FingerPoint {
        private ShapeRenderer shapeRenderer;
        private float xPos;
        private float yPos;
        private float radius;
        private int pointerIndex;

        public FingerPoint(float x, float y, int pointer) {
            shapeRenderer = new ShapeRenderer();
            xPos = x;
            yPos = y;
            pointerIndex = pointer;
            radius = 25.0f;
        }

        public void draw(Batch batch) {
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
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

        FingerPoints = new Array<FingerPoint>(10);
    }

    @Override
    public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        Iterator<FingerPoint> fpItr = FingerPoints.iterator();
        while(fpItr.hasNext()) {
            FingerPoint fp = fpItr.next();
            fp.draw(batch);
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
        if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK))
            Gdx.app.exit();
        return false;
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
        if((pointer > 0) && (pointer < 10)) {
            Iterator<FingerPoint> fpItr = FingerPoints.iterator();
            while (fpItr.hasNext()) {
                FingerPoint fp = fpItr.next();
                if (fp.getPointer() == pointer) {
                    fp.setPos(screenX, screenY);
                    return true;
                }
            }
            FingerPoints.add(new FingerPoint(screenX, screenY, pointer));
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
        if((pointer > 0) && (pointer < 10)) {
            Iterator<FingerPoint> fpItr = FingerPoints.iterator();
            while (fpItr.hasNext()) {
                FingerPoint fp = fpItr.next();
                if (fp.getPointer() == pointer) {
                    fp.setPos(screenX, screenY);
                    return true;
                }
            }
            FingerPoints.add(new FingerPoint(screenX, screenY, pointer));
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

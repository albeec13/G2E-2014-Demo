package com.wge.G2E2014;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import static com.wge.G2E2014.Helpers.Box2DHelper.PixelsToMeters;

import com.badlogic.gdx.utils.Array;
import com.wge.G2E2014.GameObjects.*;
import com.wge.G2E2014.Helpers.Platform;

import java.util.HashMap;

public class G2E_Demo implements ApplicationListener, InputProcessor {
    //one-off debug APK for media player with non-functional portrait mode
    private static final boolean DEBUG = false;

    //main Libgdx-related vars
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private BitmapFont font;
    private Texture backgroundTexture;
    private TextureRegion backgroundTextureRegion;

    // box2D physics-related vars
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera b2dCamera;
    private HashMap<Integer, FingerPoint> FingerPoints;
    private static float physicsDelta = 1/45f;
    private float accumulator = 0.0f;
    private boolean gravityOn;
    private Body wallBody;

    //touch-related vars
    private Vector3 touchVector;
    private int fingersOnScreen = 0;
    private int pointerOffset = 0;
    private int lastPointer = 0;

    //mode
    boolean curlingMode = false;

    //platform-specific accessor
    private Platform platform;

    public G2E_Demo(Platform platform){
        this.platform = platform;
    }

    @Override
    public void create () {
        Gdx.input.setInputProcessor(this);

        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();

        //set game camera, spritebatch, and font
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("Candara.fnt"),Gdx.files.internal("Candara.png"), false, true);
        font.setColor(Color.WHITE);
        font.setScale(1f,1f);
        backgroundTexture = new Texture(Gdx.files.internal("curling_sheet.png"));
        backgroundTextureRegion = new TextureRegion(backgroundTexture);

        //create box2d world, renderer, camera, and bounding box
        world = new World(new Vector2(0, 0), true);
        toggleGravity(true);
        debugRenderer = new Box2DDebugRenderer();
        b2dCamera = new OrthographicCamera(PixelsToMeters(width), PixelsToMeters(height));
        b2dCamera.setToOrtho(false, PixelsToMeters(width), PixelsToMeters(height));

        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.position.set(PixelsToMeters(0), PixelsToMeters(0));
        wallBodyDef.type = BodyDef.BodyType.StaticBody;
        wallBody = world.createBody(wallBodyDef);

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

        wallBody.setActive(!curlingMode);

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

        if(curlingMode) {
            if((Gdx.app.getType() == Application.ApplicationType.Android)&&(Gdx.graphics.getWidth() < Gdx.graphics.getHeight()))
                batch.draw(backgroundTextureRegion, 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),1, 1, 0, false);
            else
                batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1920, 1080, false, false);
        }

        for(int i = 0; i < 10; i++) {
            if(FingerPoints.containsKey(i)) {
                FingerPoints.get(i).draw(batch, curlingMode);
            }
        }

        if(!DEBUG) //remove in debug mode due to orientation on screen being wrong (too much time to fix right now)
            font.draw(batch, "Fingers: " + fingersOnScreen, 20, Gdx.graphics.getHeight() - 100);

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
        b2dCamera.setToOrtho(false, PixelsToMeters(width), PixelsToMeters(height));
        camera.setToOrtho(false, width, height);
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
        backgroundTexture.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.BACK:
                Gdx.app.exit();
                return true;
            case Input.Keys.A:
            case Input.Keys.MENU:
                /*for(Map.Entry<Integer, FingerPoint> f : FingerPoints.entrySet()) {
                    f.getValue().dispose();
                }
                FingerPoints.clear();*/
                toggleCurlingMode();
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

            lastPointer = (lastPointer + 1) % 10;

            updateFingerPoints(screenX, screenY, getPointerOffset(pointer));

            //freeze physics on finger down
            try {
                FingerPoints.get(getPointerOffset(pointer)).freeze();
            }
            catch (Exception ex) {
                System.out.println(ex);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 10) {
            fingersOnScreen--;

            //unfreeze physics on finger up
            try {
                FingerPoints.get(getPointerOffset(pointer)).unFreeze();
            }
            catch(Exception ex) {
                System.out.println(ex);
            }

            if(fingersOnScreen == 0) {
                pointerOffset = lastPointer;
            }

            if(DEBUG) {
                if((screenX < 50)&&(screenY < 50)) {
                    toggleCurlingMode();
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer < 10) {
            updateFingerPoints(screenX, screenY, getPointerOffset(pointer));
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
            //FingerPoints.get(pointer).setPos(touchVector.x, touchVector.y, Gdx.graphics.getDeltaTime());
        }
    }

    private void toggleGravity(boolean active) {
        if(!active) {
            world.setGravity(new Vector2(0f, 0f));
            gravityOn = false;
        }
        else {
            if(DEBUG)
                world.setGravity(new Vector2(-9.8f, 0f));
            else
                world.setGravity(new Vector2(0f, -9.8f));
            gravityOn = true;

            Array<Body> worldBodies = new Array<Body>();
            world.getBodies(worldBodies);

            for(Body b : worldBodies) {
                b.setAwake(true);
            }
        }
    }

    private void toggleCurlingMode() {
        curlingMode = !curlingMode;

        toggleGravity(!curlingMode);

        wallBody.setActive(!curlingMode);

        if(curlingMode)
            font.setColor(Color.BLACK);
        else
            font.setColor(Color.WHITE);
    }

    private int getPointerOffset(int pointer) {
        return ((pointer + pointerOffset) % 10);
    }

    /*private void toggleOrientation() {
        if(platform.GetOrientation() == "portrait")
            platform.SetOrientation("landscape");
        else if(platform.GetOrientation() == "landscape")
            platform.SetOrientation("portrait");
    }*/
}

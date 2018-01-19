package com.cardenask.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cardenask.main.Main;
import com.cardenask.states.LogoState;
import com.cardenask.states.States;
import com.cardenask.states.TitleState;

import java.util.EmptyStackException;
import java.util.Stack;

/** GameStateManager - the class responsible for handling the required logic the state the game is in */
public class GameStateManager {

    private Stack<States> states;
    private SpriteBatch batch;
    private ShapeRenderer sr;
    private Viewport viewport;
    private OrthographicCamera camera;

    /**
     * GameStateManager - constructor responsible for instantiating variables
     */
    public GameStateManager() {
        init();
    }

    /** init - method which instantiates neccessary variables */
    public void init() {
        batch = new SpriteBatch();
        sr = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, camera);
        states = new Stack<States>();
        setState(new LogoState(this));
    }

    /**
     * setState - method which sets the current state of the game utilizing a Stack
     * @param s - State which will be pushed onto the Stack
     */
    public void setState(States s) {
        if(states.isEmpty()) {
            states.push(s);
        } else {
            dispose(states.pop());
            states.push(s);
        }
    }

    /**
     * peek - looks at the current state on the Stack
     * @return the state which is on top of the Stack to be displayed and drawn
     */
    private States peek() {
        try {
            return states.peek();
        } catch (EmptyStackException e) {
            setState(new TitleState(this));
        }
        return states.peek();
    }

    /** drawImages - draws the images associated with the current state on the Stack */
    public void drawImages() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        peek().drawImages(batch);
        batch.end();
    }

    /** drawShapes - draws the shapes associated with the current state on the Stack */
    public void drawShapes() {
        sr.setAutoShapeType(true);
        sr.setProjectionMatrix(camera.combined);
        sr.begin();
        peek().drawShapes(sr);
        sr.end();
    }

    /** update - updates the required logic associated with the current state on the Stack */
    public void update() {
        peek().update();
    }

    /** handleInput - handles the input associated with the current state on the Stack */
    public void handleInput() {
        Gdx.input.setCatchBackKey(true);
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            setState(new TitleState(this));
        }
        peek().handleInput();
    }

    /**
     * resize - deals with resizing the camera and viewport
     * @param width - new width of the viewport
     * @param height - new height of the viewport
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    /**
     * dispose - disposes of any neccessary resources from the outgoing State
     * @param s - state which will be have its resources disposed
     */
    private void dispose(States s) { s.dispose(); }

    /**
     * getViewport - responsible for getting the current viewport
     * @return viewport of the current camera
     */
    public Viewport getViewport(){return viewport;}

    /**
     * getBatch - responsible for getting the SpriteBatch for the game
     * @return SpriteBatch of the game
     */
    public SpriteBatch getBatch(){return batch;}

    /**
     * getShapeRenderer - responsible for getting the ShapeRenderer for the game
     * @return ShapeRenderer of the game
     */
    public ShapeRenderer getShapeRenderer(){return sr;}
}

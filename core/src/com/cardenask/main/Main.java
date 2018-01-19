/**
 * Circlez:
 * A never ending pea shooter style game with various power ups and enemies
 * Made by Kevin Cardenas (KevBotStudios) in the Spring of 2017
 */

package com.cardenask.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.cardenask.handlers.GameStateManager;

/**
 * Main - class which starts the Circlez game
 * @see com.badlogic.gdx.ApplicationAdapter
 */
public class Main extends ApplicationAdapter {

    private GameStateManager gsm;
    public static int WIDTH, HEIGHT;

    @Override
    public void create() {
        WIDTH = 500;
        HEIGHT = WIDTH * 16 / 9;
        gsm = new GameStateManager();
    }

    @Override
    public void render() {
        //Clear Screen
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.handleInput();
        gsm.update();
        gsm.drawImages();
        gsm.drawShapes();
    }

    @Override
    public void resize(int width, int height){
        gsm.resize(width, height);
    }

    @Override
    public void dispose() {
        gsm.getBatch().dispose();
        gsm.getShapeRenderer().dispose();
    }
}

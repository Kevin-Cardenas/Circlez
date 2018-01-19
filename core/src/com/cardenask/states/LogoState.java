package com.cardenask.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cardenask.handlers.GameStateManager;
import com.cardenask.main.Main;

/**
 * LogoState - class which deals with the 3 second logo seen when entering the app
 * @see com.cardenask.states.States
 */
public class LogoState extends States {

    private long timeStart, goalTime;
    private float width, height, step;
    private String kev = "KEVBOT STUDIOS PRESENTS";

    public LogoState(GameStateManager gsm) {
        super(gsm);
        this.gsm = gsm;
        init();
    }

    @Override
    public void drawImages(SpriteBatch sb) {
        Gdx.gl.glClearColor(red,green,blue,alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        titleFont.setColor(new Color(1, 1, 1, step));
        titleFont.draw(sb, kev, (Main.WIDTH / 2) - (width / 2), (Main.HEIGHT / 2) - (height / 2));
    }

    @Override
    public void drawShapes(ShapeRenderer sr) {}

    @Override
    public void update() {
        step -= 1/3f * Gdx.graphics.getDeltaTime();
        //After 3 Seconds Change States
        long timeDiff = (System.nanoTime() - timeStart) / 1000000;
        if (timeDiff > goalTime) {
            gsm.setState(new TitleState(gsm));
        }
    }

    @Override
    public void handleInput() {}

    @Override
    public void init() {
        timeStart = System.nanoTime();
        goalTime = 3000; //Target Transition Time: 3 second. (3000 milliseconds)
        GlyphLayout layout = new GlyphLayout(titleFont, kev);
        width = layout.width;
        height = layout.height;
        step = 1.0f;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

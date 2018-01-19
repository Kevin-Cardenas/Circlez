package com.cardenask.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.cardenask.handlers.Buttons;
import com.cardenask.handlers.GameStateManager;
import com.cardenask.handlers.ResourceManager;
import com.cardenask.main.Main;

/**
 * HighScoreState - class which deals with viewing the player's highest score
 * @see com.cardenask.states.States
 */
public class HighScoreState extends States{

    private Button backButton;
    private Buttons buttons;
    private ResourceManager res;

    /**
     * HighScoreState - constructor which initializes a HighScore state AFTER the TitleState
     * @param gsm - reference to the GameStateManager
     */
    public HighScoreState(GameStateManager gsm) {
        super(gsm);
        this.gsm = gsm;
        init();
    }


    @Override
    public void update() {
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void handleInput() {
        if(backButton.isPressed()){
            res.setPlaying(false);
            res.stopMusic("Highscores", true);
            gsm.setState(new TitleState(gsm));
        }
    }

    @Override
    public void drawShapes(ShapeRenderer sr) {
        stage.draw();
    }

    @Override
    public void drawImages(SpriteBatch sb) {
        Gdx.gl.glClearColor(red, green, blue, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        String highScoreString = "HIGH SCORE: " + prefs.getInteger("highscore");
        GlyphLayout layout = new GlyphLayout(titleFont, highScoreString);
        titleFont.draw(sb, highScoreString, Main.WIDTH / 2 - layout.width / 2, Main.HEIGHT / 2 - layout.height / 2);
    }

    @Override
    public void init() {
        buttons = new Buttons();
        backButton = buttons.createButton("Images/Back.png", "Back_Button");
        backButton.setSize(50, 50);
        backButton.setPosition(Main.WIDTH / 16 - backButton.getWidth() / 2, Main.HEIGHT / 32 - backButton.getHeight() / 2);
        stage.addActor(backButton);
        res = new ResourceManager();
        res.loadMusic("Music/HighScore.mp3", "Highscores");
        if(!TitleState.MUTE_SOUND) {
            res.setPlaying(true);
            res.startMusic("Highscores", true);
        }
    }

    @Override
    public void dispose() {
        res.disposeMusic("Highscores");
        stage.dispose();
        buttons.dispose();
    }
}

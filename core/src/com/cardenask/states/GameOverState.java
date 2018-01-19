package com.cardenask.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.cardenask.entities.Enemy;
import com.cardenask.handlers.Buttons;
import com.cardenask.handlers.GameStateManager;
import com.cardenask.main.Main;

import java.util.ArrayList;

/**
 * GameOverState - class which handles the state when the Player dies
 *               this should only ever be instantiated AFTER a PlayState
 * @see com.cardenask.states.States
 */
public class GameOverState extends States {

    private int score;
    private ArrayList<Enemy> enemies;
    private GlyphLayout layout;
    private String scoreText;
    private Buttons buttons;
    private Button playButton;

    /**
     * GameOverState - constructor which
     * @param gsm - reference to the GameStateManager
     * @param score - score of the game that was just played. Only passed in from PlayState
     */
    public GameOverState(GameStateManager gsm, int score){
        super(gsm);
        this.gsm = gsm;
        this.score = score;
        init();
    }

    @Override
    public void drawShapes(ShapeRenderer sr) {
        for(Enemy e : enemies) {
            e.drawShapes(sr);
        }
        stage.draw();
    }

    @Override
    public void drawImages(SpriteBatch sb) {
        Gdx.gl.glClearColor(red,green,blue,alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        titleFont.draw(sb, scoreText, Main.WIDTH / 2 - layout.width / 2, Main.HEIGHT / 2 - layout.height / 2);
    }

    @Override
    public void update() {
        for(Enemy e : enemies) {
            e.update();
        }

        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void handleInput() {
        if(playButton.isPressed()){
            res.setPlaying(false);
            gsm.setState(new TitleState(gsm));
        }
    }

    @Override
    public void init() {
        scoreText = "S C O R E : " + score;
        layout = new GlyphLayout(titleFont, scoreText);
        buttons = new Buttons();
        playButton = buttons.createButton("Images/Play.png", "Play");
        playButton.setSize(50, 50);
        playButton.setPosition(Main.WIDTH / 2 - playButton.getWidth() / 2, Main.HEIGHT / 8 - playButton.getHeight() / 2);
        stage.addActor(playButton);
        //Create some enemies to fly around the screen
        enemies = new ArrayList<Enemy>();
        for(int i = 0; i < 3; i++){
            enemies.add(new Enemy(1,1, true));
            enemies.add(new Enemy(1,2, true));
            enemies.add(new Enemy(2,1, true));
            enemies.add(new Enemy(2,2, true));
            enemies.add(new Enemy(3,1, true));
            enemies.add(new Enemy(3,2, true));
            enemies.add(new Enemy(4,1, true));
            enemies.add(new Enemy(5,1, true));
        }

        if(!TitleState.MUTE_SOUND) {
            res.loadMusic("Music/Over.mp3", "Over");
            res.setPlaying(true);
            res.startMusic("Over", true);
        }

        //Deal with high score changes
        if(score > prefs.getInteger("highscore")) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        buttons.dispose();
        if(!TitleState.MUTE_SOUND) res.stopMusic("Over", true);
    }
}

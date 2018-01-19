package com.cardenask.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.cardenask.entities.Enemy;
import com.cardenask.handlers.Buttons;
import com.cardenask.handlers.GameStateManager;
import com.cardenask.handlers.TextButtons;
import com.cardenask.main.Main;

import java.util.ArrayList;

/**
 * TitleState - state which is responsible for the title screen for the game
 * @see com.cardenask.states.States
 */
public class TitleState extends States {

    private ArrayList<Enemy> enemies;
    private String titleOptions[] = {"S T A R T", "INSTRUCTIONS", "HIGH SCORE", "QUIT"};
    private TextButtons textButtons;
    private Buttons buttons;
    private Button muteButton, unmuteButton;
    public static boolean MUTE_SOUND;

    /**
     * TitleState - constructor which instantiates neccessary variables
     * @param gsm - the reference of the GameStateManager
     */
    public TitleState(GameStateManager gsm) {
        super(gsm);
        this.gsm = gsm;
        init();
    }

    @Override
    public void drawShapes(ShapeRenderer sr) {
        stage.draw();
        for(Enemy e : enemies) {
            e.drawShapes(sr);
        }
    }

    @Override
    public void drawImages(SpriteBatch sb) {
        Gdx.gl.glClearColor(red, green, blue, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void update() {
        //Check to see whether to mute the sound
        if (MUTE_SOUND) {
            res.setPlaying(false);
            res.pauseMusic("Title_Music");
        }
        if (!MUTE_SOUND && !res.isPlaying()) {
            res.setPlaying(true);
            res.startMusic("Title_Music", true);
        }
        //Update the enemies on the loading screen
        for(Enemy e : enemies) {
            e.update();
        }

        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void handleInput() {
        textButtons.titlePress();

        //Start Button Pressed
        if (textButtons.getStart()) {
            if (!MUTE_SOUND) {
                res.playSound("Return");
                res.stopMusic("Title_Music", true);
            }
            gsm.setState(new PlayState(gsm));
        }

        //Instructions Button Pressed
        if (textButtons.getInstructions()) {
            if (!MUTE_SOUND) {
                res.playSound("Select");
            }
            gsm.setState(new InstructionState(gsm));
        }

        //High Scores Button Pressed
        if (textButtons.getScores()) {
            gsm.setState(new HighScoreState(gsm));
        }

        //Quit Button Pressed
        if (textButtons.getQuit()) {
            if (!MUTE_SOUND) {
                res.stopMusic("Title_Music", true);
            }
            Gdx.app.exit();
        }

        //Mute Button Pressed
        if (muteButton.isPressed()) {
            MUTE_SOUND = true;
            muteButton.remove();
            stage.addActor(unmuteButton);

        }
        //Un-mute Button Pressed
        if (unmuteButton.isPressed()) {
            MUTE_SOUND = false;
            unmuteButton.remove();
            stage.addActor(muteButton);
        }
    }

    @Override
    public void init() {
        String title = "C I R C L E Z";
        //Creating Enemies ArrayList for the loading screen
        enemies = new ArrayList<Enemy>();

        textButtons = new TextButtons(textFont);
        textButtons.buttonCreation(titleOptions, "Title Options", "Images/Text_Button_Up.png", stage);
        TextButtons circlezButton = new TextButtons(circlezFont);
        circlezButton.buttonCreation(title, "Images/Text_Button_Up.png" , stage);

        Gdx.input.setInputProcessor(stage);
        //Enemies on the TitleScreen
        for (int i = 0; i < 3; i++) {
            enemies.add(new Enemy(1, 1, true));
            enemies.add(new Enemy(1, 2, true));
            enemies.add(new Enemy(2, 1, true));
            enemies.add(new Enemy(2, 2, true));
            enemies.add(new Enemy(3, 1, true));
            enemies.add(new Enemy(3, 2, true));
            enemies.add(new Enemy(4, 2, true));
            enemies.add(new Enemy(5, 2, true));
        }


        buttons = new Buttons();
        unmuteButton = buttons.createButton("Images/Unmute.png", "Unmute");
        unmuteButton.setSize(50, 50);
        unmuteButton.setPosition(Main.WIDTH / 16 - unmuteButton.getWidth() / 2, Main.HEIGHT / 32 - unmuteButton.getHeight() / 2);
        muteButton = buttons.createButton("Images/Mute.png", "Mute");
        muteButton.setSize(50, 50);
        muteButton.setPosition(Main.WIDTH / 16 - muteButton.getWidth() / 2, Main.HEIGHT / 32 - muteButton.getHeight() / 2);
        stage.addActor(muteButton);

        res.loadMusic("Music/Title.mp3", "Title_Music");
        res.setPlaying(true);
        res.startMusic("Title_Music", true);
        res.changeVolume("Title_Music", 0.6f);
        res.loadSound("Sound/select.wav", "Select");
        res.loadSound("Sound/return.wav", "Return");

        MUTE_SOUND = false;
    }

    @Override
    public void dispose() {
        textButtons.dispose();
        stage.dispose();
        buttons.dispose();
        if (!MUTE_SOUND) {
            res.disposeSound("Select");
            res.disposeSound("Return");
            res.disposeMusic("Title_Music");
        }
    }
}

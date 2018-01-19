package com.cardenask.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.cardenask.handlers.Buttons;
import com.cardenask.handlers.GameStateManager;
import com.cardenask.handlers.ResourceManager;
import com.cardenask.main.Main;

/** InstructionState - class which is responsible for drawing the instructions */
public class InstructionState extends States {

    private int r;

    private String[] instructionTexts = {"INSTRUCTIONS:", "* COLLECT SQUARES", "* KILL ENEMIES", "* SURVIVE"};
    private String[] powerupTexts = {"PowerUp", "Slow Down Time", "Extra Life (RARE)"};

    private Button backButton;
    private Buttons button;
    private Color color;

    /**
     * InstructionState - constructor which instantiates the neccessary variables
     * @param gsm - reference to the GameStateManager
     */
    public InstructionState(GameStateManager gsm){
        super(gsm);
        this.gsm = gsm;
        init();
    }


    /** labelCreation - draws the power up labels */
    private void labelCreation(){
        Label.LabelStyle style = new Label.LabelStyle(titleFont, Color.WHITE);
        GlyphLayout layout = new GlyphLayout(titleFont, "FRIENDLY SQUARES:");
        Label friendlyCircleLabel = new Label("FRIENDLY SQUARES:", style);
        friendlyCircleLabel.setPosition(Main.WIDTH / 2 - layout.width / 2, Main.HEIGHT / 2 + Main.HEIGHT / 16 - layout.height / 2);
        stage.addActor(friendlyCircleLabel);

        Label[] labels = new Label[powerupTexts.length];
        for(int i = 0; i < powerupTexts.length; i++){
            layout = new GlyphLayout(titleFont, powerupTexts[i]);
            labels[i] = new Label(powerupTexts[i], style);
            labels[i].setPosition(Main.WIDTH / 4, Main.HEIGHT / 2 - layout.height / 2 - (i * 55));
            stage.addActor(labels[i]);
        }

        //Array Label Creation for instruction texts (overwriting labels)
        labels = new Label[instructionTexts.length];
        style = new Label.LabelStyle(titleFont, Color.RED);
        for(int i = 0; i < instructionTexts.length; i++){
            labels[i] = new Label(instructionTexts[i], style);
            labels[i].setPosition(Main.WIDTH / 2, Main.HEIGHT - Main.HEIGHT / 8 - (i * 50), Align.center);
            stage.addActor(labels[i]);
        }
    }

    @Override
    public void drawShapes(ShapeRenderer sr) {
        //Drawing Sample PowerUps
        for(int i = 0; i < 3; i++){
            if(i == 0){
                color = new Color(1.0f, 11 / 255f, 252 / 255f, 1.0f);
            }
            if(i == 1){
                color = new Color(0f, 1.0f, 1.0f, 1.0f);
            }
            if(i == 2){
                color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
            }
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(color);
            sr.rect(Main.WIDTH / 8, Main.HEIGHT / 2 - (i * 57), r, r);
        }
        stage.draw();
    }

    @Override
    public void drawImages(SpriteBatch sb) {
        Gdx.gl.glClearColor(red, green, blue, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void update() {
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void handleInput() {
        if(backButton.isPressed()){
            res.setPlaying(false);
            res.stopMusic("Instructions", true);
            gsm.setState(new TitleState(gsm));
        }
    }

    @Override
    public void init() {
        res = new ResourceManager();
        res.loadMusic("Music/Instructions.mp3", "Instructions");
        if(!TitleState.MUTE_SOUND) {
            res.setPlaying(true);
            res.startMusic("Instructions", true);
        }

        button = new Buttons();
        backButton = button.createButton("Images/Back.png", "Back Button");
        r = 18;
        stage.addActor(backButton);
        backButton.setSize(50, 50);
        backButton.setPosition(Main.WIDTH / 16 - backButton.getWidth() / 2, Main.HEIGHT / 32 - backButton.getHeight() / 2);
        labelCreation();
    }

    @Override
    public void dispose() {
        stage.dispose();
        res.disposeMusic("Instructions");
        button.dispose();
    }
}

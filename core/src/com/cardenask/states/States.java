package com.cardenask.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cardenask.handlers.GameStateManager;
import com.cardenask.handlers.ResourceManager;

/**
 * States - abstract class which has the neccessary properties
 * @see com.cardenask.handlers.GameStateManager
 */
public abstract class States{

    protected GameStateManager gsm;
    protected ResourceManager res;
    protected Preferences prefs;
    protected float red, green, blue, alpha;
    protected BitmapFont textFont, titleFont, circlezFont;
    protected Stage stage;

    /**
     * States - constructor which instantiates the neccessary variables shared across States
     * @param gsm - reference to the GameStateManager for each state
     */
    public States(GameStateManager gsm) {
        this.gsm = gsm;
        res = new ResourceManager();

        prefs = Gdx.app.getPreferences("Circle Defender");
        prefs.putInteger("highscore", 0);

        red = 0 / 255f;
        green = 100 / 255f;
        blue = 255f / 255f;
        alpha = 0 / 255f;

        textFont = new BitmapFont(Gdx.files.internal("Font/8_Bit_Font.fnt"));
        titleFont = new BitmapFont(Gdx.files.internal("Font/8_Bit_Font_36.fnt"));
        circlezFont = new BitmapFont(Gdx.files.internal("Font/8_Bit_Font_40.fnt"));

        stage = new Stage(gsm.getViewport(), gsm.getBatch());
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * drawShapes - using the ShapeRenderer, we will draw the Player and enemies on the screen
     * @see ShapeRenderer
     * @param sr - ShapeRenderer responsible for drawing the required entities
     */
    public abstract void drawShapes(ShapeRenderer sr);

    /**
     * drawImages - using the SpriteBatch, we will draw the required images
     * @see SpriteBatch
     * @param sb - SpriteBatch responsible for drawing the required entities
     */
    public abstract void drawImages(SpriteBatch sb);

    /** update - method which updates the current state's game logic */
    public abstract void update();

    /** handleInput - method which handles the input from the current state */
    public abstract void handleInput();

    /** init - method which instantiates required variables */
    public abstract void init();

    /** dispose - method which disposes neccessary resources */
    public abstract void dispose();
}

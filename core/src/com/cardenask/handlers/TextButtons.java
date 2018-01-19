package com.cardenask.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.cardenask.main.Main;

/**
 * TextButtons - class which deals with creating
 * @see TextButton
 */
public class TextButtons {

    private TextButton[] buttons;
    private TextButton button;
    private TextButton.TextButtonStyle style;
    private Skin skin;
    private BitmapFont font;
    private boolean start, instructions, scores, quit;

    /**
     * TextButtons - constructor which instantiates the neccessary variables
     * @param font - font which will be used to draw the text on buttons
     */
    public TextButtons(BitmapFont font){
        this.font = font;
        skin = new Skin();
        start = instructions = scores = quit = false;
    }

    /**
     * buttonCreation - overloaded method. Given an array of Strings, the buttons will be created
     * @param options - array of Strings representing the options to be created
     * @param key - String representation of the Skin for the button
     * @param path - String representation of the file location for the Skin
     * @param stage - stage which the buttons will be drawn onto
     */
    public void buttonCreation(String[] options, String key, String path, Stage stage){
        buttons = new TextButton[options.length];
        skin.add(key, new Texture(path));
        style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.up = skin.getDrawable(key);
        for(int i = 0; i < options.length; i++){
            buttons[i] = new TextButton(options[i], style);
            stage.addActor(buttons[i]);
            buttons[i].setPosition(Main.WIDTH / 2, Main.HEIGHT - Main.HEIGHT / 2 - ((i * buttons[i].getHeight())), Align.center);
        }
    }

    /**
     * buttonCreation - overloaded method. Creates a single button with the key acting as the option
     * @param key - String representation of the option and the key for getting the Skin
     * @param path - String representation of the file location for the Skin
     * @param stage - stage which the buttons will be drawn onto
     */
    public void buttonCreation(String key, String path, Stage stage){
        skin.add(key, new Texture(path));
        style = new TextButton.TextButtonStyle();
        style.up = skin.getDrawable(key);
        style.down = skin.getDrawable(key);
        style.font = font;
        style.fontColor = Color.GREEN;
        button = new TextButton(key, style);
        stage.addActor(button);
        button.setPosition(Main.WIDTH / 2, Main.HEIGHT - Main.HEIGHT / 4, Align.center);
    }

    /**
     * backCreation - creates the back button which is on the screen in various places regardless if a Stage is on or not
     * @param key - String representation of the name of the back button
     * @param path - String representation of the file's location
     * @return new TextButton which is the back button
     */
    public TextButton backCreation(String key, String path){
        skin.add(key, new Texture(path));
        style = new TextButton.TextButtonStyle();
        style.up = skin.getDrawable(key);
        style.font = font;
        style.fontColor = Color.BLACK;
        button = new TextButton(key, style);
        return button;
    }

    /** titlePress - helper method which sets flags to true depending on which button was pressed */
    public void titlePress(){
        if(buttons[0].isPressed()){ start = true; }
        if(buttons[1].isPressed()){ instructions = true; }
        if(buttons[2].isPressed()){ scores = true; }
        if(buttons[3].isPressed()){ quit = true; }
    }

    /** dispose - disposes resources */
    public void dispose(){
        skin.dispose();
    }

    /** @return true if start was pressed. If start was not pressed, return false; */
    public boolean getStart(){ return start; }
    /** @return true if instructions was pressed. If instructions was not pressed, return false; */
    public boolean getInstructions(){ return instructions; }
    /** @return true if scores was pressed. If scores was not pressed, return false; */
    public boolean getScores(){ return scores; }
    /** @return true if quit was pressed. If quit was not pressed, return false; */
    public boolean getQuit(){ return quit; }
}

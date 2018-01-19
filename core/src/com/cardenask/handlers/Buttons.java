package com.cardenask.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Buttons - class which makes the Button and Skin
 * @see Button
 * @see Skin
 */
public class Buttons {

    private Skin skin;

    /** Buttons - constructor which instantiates the neccessary variables */
    public Buttons(){
        skin = new Skin();
    }

    /**
     * createButton - creates a new button with give path and key to the Skin of the button
     * @param path - String for the file location of the Skin for the Button
     * @param key - String for the key of the Skin for the Button
     * @return Button to be returned with the Skin and properties
     */
    public Button createButton(String path, String key){
        skin.add(key, new Texture(path));
        Drawable knob = skin.getDrawable(key);
        Button button = new Button(knob);
        return button;
    }

    /** dispose - disposes the Skin */
    public void dispose(){ if(skin != null) skin.dispose(); }
}

package com.cardenask.handlers;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * JoyStick - class which deals with the Touchpad and JoyStick for the game
 * @see Touchpad
 */
public class JoyStick {

    private Touchpad touchPad;

    /** JoyStick - constructor which instantiates neccessary variables */
    public JoyStick(){
        Skin touchPadSkin = new Skin();
        touchPadSkin.add("Touch Knob", new Texture("Images/JoyStick.png"));
        Touchpad.TouchpadStyle touchPadStyle = new Touchpad.TouchpadStyle();
        Drawable touchKnob = touchPadSkin.getDrawable("Touch Knob");
        touchPadStyle.knob = touchKnob;
        touchPad = new Touchpad(10, touchPadStyle);
        touchPad.setBounds(15, 15, 200, 200);
    }

    /** @return Touchpad for the game */
    public Touchpad getTouchPad(){ return touchPad; }
}

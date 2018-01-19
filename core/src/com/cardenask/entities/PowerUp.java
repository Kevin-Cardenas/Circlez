package com.cardenask.entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cardenask.main.Main;

/**
 * PowerUp - class which deals with the different types of the PowerUps and their properties
 * @see com.cardenask.entities.GameObject
 */
public class PowerUp extends GameObject{

    private Color color;
    private int type, pad, r;

    /**
     * PowerUp - constructor which initializes the neccessary characteristics of each PowerUp
     * @param type - int representation of the type of the PowerUp. Type 1: PowerUp for the Player
     *             Type 2: Slowdown the enemies. Type 3: Give an extra life for the Player
     * @param x - starting x coordinate for the PowerUp
     * @param y - starting y coordinate for the PowerUp
     */
    public PowerUp(int type, float x, float y){
        this.type = type;
        this.x = x;
        this.y = y;
        init();
    }

    /**
     * update - this method updates the movement of the PowerUp (should only move along Y axis)
     * @return true if the PowerUp is offscreen and needs to get removed. False if the PowerUp is
     *         still on the screen
     */
    public boolean update(){
        y += dy;
        //Goes a little offscreen then remove
        if(y < -(r + pad) || x > Main.WIDTH - r + pad || x < r + pad) return true;
        return false;
    }

    @Override
    public void drawShapes(ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(x, y, r + pad, r + pad);
    }

    @Override
    public void init(){
        dy = -2;
        pad = 3;
        r = 7;
        //Type 1: PowerUp Meter Pinkish Color
        if(type == 1){
            color = new Color(1.0f, 11 / 255f, 252 / 255f, 1.0f);
        }
        //Type 2: SlowDown Time Green Color
        if(type == 2){
            color = new Color(0f, 1.0f, 1.0f, 1.0f);
        }
        //Type 3: Add extra life same colors as player
        if(type == 3){
            color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    /** @return x coordinate of the PowerUp */
    public float getX(){return x;}
    /** @return y coordinate of the PowerUp */
    public float getY(){return y;}
    /** @return radius of the PowerUp */
    public float getR(){return r;}
    /** @return int representation of the type */
    public int getType(){return type;}
}

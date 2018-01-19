package com.cardenask.visuals;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cardenask.entities.Enemy;
import com.cardenask.main.Main;

import java.util.ArrayList;

/**
 * SlowDown - class which deals with the logic of slowing down the game after the power up is collected
 * @see com.cardenask.entities.PowerUp
 */
public class SlowDown {

    private float slowDownTimer, slowDownTimerDiff;
    private int slowDownLength;
    private boolean slowDown;

    /** SlowDown - constructor which instantiates variables */
    public SlowDown(){
        slowDownTimer = 0;
        slowDownTimerDiff = 0;
        slowDownLength = 6000;
        slowDown = false;
    }

    /**
     * update - updates the enemies by slowing them down or speeding them up. Slow down periods should be 6 seconds
     * @param enemies - the ArrayList<Enemy></Enemy> which will get modified
     */
    public void update(ArrayList<Enemy> enemies){
        //If the timer has started then slow down the enemies
        if(slowDownTimer != 0){
            slowDown = true;
            slowDownTimerDiff = (System.nanoTime() - slowDownTimer) / 1000000;
            for(int i = 0; i < enemies.size(); i++){
                enemies.get(i).setSlow(true);
            }
        }
        //If the timer difference is greater than 6 seconds reset everything
        if(slowDownTimerDiff > slowDownLength){
            slowDownTimer = 0;
            slowDown = false;
            for(int i = 0; i < enemies.size(); i++){
                enemies.get(i).setSlow(false);
            }
        }
    }

    /**
     * drawShapes - draws the necessary shapes
     * @param sr - ShapeRenderer responsible for drawing the shapes
     */
    public void drawShapes(ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);
        sr.rect(Main.WIDTH - Main.WIDTH / 3, Main.HEIGHT - Main.HEIGHT / 13, 140, 10);
        sr.setColor(new Color(178 / 255f, 181 / 255f, 186 / 255f, 1));
        sr.rect(Main.WIDTH - Main.WIDTH / 3, Main.HEIGHT - Main.HEIGHT / 13, 140 - (140 * slowDownTimerDiff / slowDownLength), 10);

    }
    /** slowStart - start the timestamp for when a slowdown PowerUp is hit */
    public void slowStart(){ slowDownTimer = System.nanoTime(); }
    /** @return true if a slow down period is active. Return false if it it normal speed */
    public boolean isSlowDown(){ return slowDown; }
}

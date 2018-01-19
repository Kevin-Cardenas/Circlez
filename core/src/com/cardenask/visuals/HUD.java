package com.cardenask.visuals;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HUD {

    private float x, y, r;
    private int pad;

    /**
     * HUD - constructor for the heads up display which draws the lives and PowerUp status
     * @param x - x coordinate where it will be drawn
     * @param y - y coordinate where it will be drawn
     * @param r - radius of the lives and power ups to be drawn on the screen
     */
    public HUD(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
        pad = 3;
    }

    /**
     * drawShapes - responsible for drawing the shapes
     * @param sr - ShapeRenderer for drawing the shapes
     * @param health - int representation of the amount of health the Player has
     * @param index - int representation of the current power up level
     * @param level - int representation of the current max power up level
     */
    public void drawShapes(ShapeRenderer sr, int health, int index, int level) {
        //Draw Lives
        for (int i = 0; i < health; i++) {
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(new Color(178 / 255f, 181 / 255f, 186 / 255f, 1));
            sr.circle(x + (i * 30), y, r + pad);
            sr.setColor(Color.WHITE);
            sr.circle(x + (i * 30), y, r);
        }
        //Draw Vacant Squares
        for (int i = 0; i < level * 2; i++){
            Gdx.gl.glLineWidth(1);
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.setColor(new Color(1.0f, 11 / 255f, 252 / 255f, 1.0f));
            sr.rect(x - 10 + (i * 30), y - 50, 20, 20);
        }
        //Draw Filled Squares
        for (int i = 0; i < index; i++){
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(new Color(1.0f, 11 / 255f, 252 / 255f, 1.0f));
            sr.rect(x - 10 + (i * 30), y - 50, 20, 20);
        }
    }
}

package com.cardenask.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cardenask.main.Main;

/**
 * Bullet - class which creates each Bullet the Player fires
 * @see com.cardenask.entities.GameObject
 */
public class Bullet extends GameObject{

    private Color color;
    private int r;

    /**
     * Bullet - constructor which instantiates the neccessary logic for the trajectory and position of the Bullet
     * @param angle - starting angle, in degrees, which the Bullet will be fired
     * @param x - starting x coordinate which the Bullet will be fired from
     * @param y - starting y coordinate which the Bullet will be fired from
     * @param r - radius of the Bullet
     */
    public Bullet(float angle, float x, float y, int r){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.r = (int)(r * .40);
        rad = (float) Math.toRadians(angle);
        speed = 5;
        dx = (float)(Math.cos(rad) * speed);
        dy = (float)(Math.sin(rad) * speed);
        color = Color.WHITE;
    }

    @Override
    public void drawShapes(ShapeRenderer sr) {
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.circle(getX(), getY(), (int)getR());
    }

    /**
     * update - updates the position of the Bullet and determines whether it is on or off screen
     * @return true if the Bullet needs to be taken OFF the screen
     *         false if the Bullet should stay ON the screen
     */
    public boolean update(){
        x += dx;
        y += dy;
        if(x > Main.WIDTH + r || x < -r || y > Main.HEIGHT + r || y < -r){
            return true;
        }
        return false;
    }


    @Override
    public void init() {}

    /** @return x coordinate */
    public float getX(){return x;}
    /** @return y coordinate */
    public float getY(){return y;}
    /** @return radius of the Bullet */
    public float getR(){return r;}
}

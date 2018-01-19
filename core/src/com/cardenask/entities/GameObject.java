package com.cardenask.entities;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** GameObject - abstract class which houses the neccessary variables and methods for a GameObject */
public abstract class GameObject {

    //Movement Variables
    protected float x, y, dx, dy, angle, rad, speed;
    protected int health, r;

    /**
     * drawShapes - using a ShapeRenderer, this method will draw the GameObject entities
     * @param sr - ShapeRenderer which will
     */
    public abstract void drawShapes(ShapeRenderer sr);

    /** init - method responsible for initializing variables */
    public abstract void init();

}

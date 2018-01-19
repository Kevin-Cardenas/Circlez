package com.cardenask.visuals;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Explosion - class which deals with the representation of the explosions */
public class Explosion {

    private float step, maxRadius, x, y, r;
    private boolean player;

    /**
     * Explosion - constructor which instantiates the neccessary variables
     * @param x - the x coordinate the Explosion will start at
     * @param y - the y coordinate the Explosion will start at
     * @param r - the radius of the circle the Explosion will start at
     * @param player - if true, the Explosion is coming from the Player.
     *               if false, the Explosion is going to an Enemy.
     */
    public Explosion(float x, float y, float r, boolean player) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.player = player;
        init();
    }

    /** init - instantiates neccessary variables */
    public void init() {
        step = 1;
        maxRadius = 60;
    }

    /**
     * update - updates the radius of the ring around the Enemy or Player
     * @return true if the radius of the ring is bigger then the maxRadius and must be removed
     *         false if the radius is less then the maxRadius and should continue to get bigger
     */
    public boolean update() {
        r += step;
        if(r > maxRadius) return true;
        return false;
    }

    /**
     * drawShapes - draws the Explosion associated with the Player or Enemy
     * @param sr - ShapeRenderer to draw the shapes
     */
    public void drawShapes(ShapeRenderer sr) {
        Color color;
        if(player){
            color = new Color(1, 0, 0, 1);
            Gdx.gl.glLineWidth(4);
            sr.setColor(color);
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.circle(x, y, getR());
        }else{
            color = new Color(1, 1, 1, 1);
            Gdx.gl.glLineWidth(4);
            sr.setColor(color);
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.circle(x, y, getR());
        }

    }
    /** @return radius of the Explosion */
    public float getR() {return r;}

}

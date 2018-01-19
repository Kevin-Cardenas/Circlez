package com.cardenask.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cardenask.main.Main;

import java.util.ArrayList;

/**
 * Player - class which houses the neccessary logic for the Player
 *        there should only be one instance of Player per game
 * @see com.cardenask.entities.GameObject
 */
public class Player extends GameObject {

    private long shootingDelay, shootingStart, shootingDiff;
    private boolean dead, recovery;
    private int pad; //Padding for the outside ring
    private long recoveryStart, recoveryDiff, recoveryTarget;

    /**
     * Player - constructor which instantiates the neccessary variables
     *        from the super class and this class
     */
    public Player() {
        super();
        init();
    }

    @Override
    public void drawShapes(ShapeRenderer sr) {
       if(!recovery) {
           sr.set(ShapeRenderer.ShapeType.Filled);
           sr.setColor(new Color(178 / 255f, 181 / 255f, 186 / 255f, 1));
           sr.circle(getX(), getY(), r + 3);
           sr.setColor(Color.WHITE);
           sr.circle(getX(), getY(), r);
       }else{
           sr.set(ShapeRenderer.ShapeType.Filled);
           sr.setColor(new Color(183 / 255f, 17 / 255f, 17 / 255f, 1));
           sr.circle(getX(), getY(), r + 3);
           sr.setColor(Color.RED);
           sr.circle(getX(), getY(), r);
       }
    }

    /**
     * update - updates the position of the Player while also
     *        checking collision with the walls
     */
    public void update() {
        x += dx;
        y += dy;
        //Collision Detection With Walls
        if(x > Main.WIDTH - (r + pad)){
            x = Main.WIDTH - (r + pad);
        }
        if(x < r + pad){
            x = r + pad;
        }
        if(y > Main.HEIGHT - (r + pad)){
            y = Main.HEIGHT - (r + pad);
        }
        if(y < Main.HEIGHT / 4 - r - pad){
            y = Main.HEIGHT / 4 - r - pad;
        }

        if(recovery){
            recovering();
        }

    }
    /** recovering - responsible for handling recovery time, 2 seconds, after Player is hit */
    private void recovering(){
        recoveryDiff = (System.nanoTime() - recoveryStart) / 1000000;
        if(recoveryDiff > recoveryTarget){
            recovery = false;
            recoveryStart = 0;
        }
    }

    /**
     * fireBullet - responsible for taking in ArrayList<Bullet></Bullet> and level and deciding how many
     *            bullets the Player can shoot at once
     * @param b - ArrayList<Bullet></Bullet> which will be modified to add the bullets the Player can shoot
     * @param level - int representation of the level the Player is (1, 2, 3). A level 1 Player can only shoot
     *              one Bullet at a time. A level 2 Player can shoot two bullets at a time. A level 3 Player can
     *              shoot 3 bullets at a time.
     */
    public void fireBullet(ArrayList<Bullet> b, int level){
        shootingDiff = (System.nanoTime() - shootingStart) / 1000000;
        if(shootingDiff > shootingDelay && level == 1){
            b.add(new Bullet(90, getX(), getY(), (int)getR()));
            shootingStart = System.nanoTime();
        }
        if(shootingDiff > shootingDelay && level == 2){
            b.add(new Bullet(90, getX() - 10, getY(), (int)getR()));
            b.add(new Bullet(90, getX() + 10, getY(), (int)getR()));
            shootingStart = System.nanoTime();
        }
        if(shootingDiff > shootingDelay && level == 3){
            b.add(new Bullet(85, getX() + 10, getY(), (int)getR()));
            b.add(new Bullet(90, getX(), getY(), (int)getR()));
            b.add(new Bullet(95, getX() - 10, getY(), (int)getR()));
            shootingStart = System.nanoTime();
        }
    }

    /** hit - method which modifies the health and recovery of the Player */
    public void hit(){
        health--;
        recovery = true;
        recoveryStart = System.nanoTime();
        if(health < 0){
            dead = true;
        }
    }

    /** addLife - method which adds one more life to the Player */
    public void addLife(){health++;}

    @Override
    public void init() {
        x = Main.WIDTH / 2;
        y = Main.HEIGHT / 2;
        r = (int)(Main.WIDTH * .0125);
        dx = 0;
        dy = 0;
        pad = 3;
        health = 3;
        shootingStart = System.nanoTime();
        shootingDelay = 250;
        shootingDiff = 0;
        recoveryStart = System.nanoTime();
        recoveryTarget = 2000;//Invincible for 2000 milliseconds (2 Seconds)
        recoveryDiff = 0;
        recovery = false;
        dead = false;
    }

    /** @return x coordinate of the Player */
    public float getX(){return x + pad;}
    /** @return y coordinate of the Player */
    public float getY(){return y + pad;}
    /** @return Radius of the Player */
    public float getR(){return r + pad;}
    /** @return health remaining of the Player */
    public int getHealth(){return health;}
    /** @return true if the Player is dead. If Player is dead returns false */
    public boolean isDead(){return dead;}
    /** @return true if the Player is recovering. If Player is not recovering return false */
    public boolean isRecovery(){return recovery;}

    /**
     * setDx - setter which sets the new Player's dx
     * @param f - difference in x for the Player
     */
    public void setDx(float f){dx = f;}
    /**
     * setDy - setter which sets the new Player's dy
     * @param f - difference in y for the Player
     */
    public void setDy(float f){dy = f;}
}

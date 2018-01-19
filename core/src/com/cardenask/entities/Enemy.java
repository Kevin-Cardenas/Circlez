package com.cardenask.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.cardenask.main.Main;

import java.util.ArrayList;

/**
 * Enemy - class which the various different types of enemies are created based on type and rank
 * @see com.cardenask.entities.GameObject
 */
public class Enemy extends GameObject{

    private int type, rank, pad;
    private float hitTimerStart, hitTargetTime, hitTimerDiff;
    private boolean dead, slow, hit, title;
    private Color color, rimColor;

    /**
     * Enemy - constructor which instantiates each unique Enemy's type and rank
     * @param type - int representation from 1 - 5. Types have different colors
     * @param rank - int representation from 1 - 2. Ranks, generally, increase the difficulty as it
     *             increases
     * @param title - boolean to show if the enemies are floating on a title screen (GameOver, Title etc.)
     *              If true, then the enemies will bounce across the entire screen. If false, then they will
     *              be bounded to the certain play area
     */
    public Enemy(int type, int rank, boolean title){
        this.type = type;
        this.rank = rank;
        this.title = title;
        enemyRanks();
        init();
    }

    /** update - responsible for updating the Enemy's position while also handling collision with walls */
    public void update(){
        if(slow){
            x += (dx * 0.30);
            y += (dy * 0.30);
        }else {
            x += dx;
            y += dy;
        }
        //Check Collision with the walls if we are on the title screen
        if(title) {
            if (x > Main.WIDTH - r - pad && dx > 0) dx = -dx;
            if (x < r && dx < 0) dx = -dx;
            if (y > Main.HEIGHT - r - pad && dy > 0) dy = -dy;
            if (y < r && dy < 0) dy = -dy;
        }else{
            if (x > Main.WIDTH - r - pad && dx > 0) dx = -dx;
            if (x < r && dx < 0) dx = -dx;
            if (y > Main.HEIGHT - r - pad && dy > 0) dy = -dy;
            if (y < Main.HEIGHT / 4 - r - pad && dy < 0) dy = -dy;
        }

        hitTimerDiff = (System.nanoTime() - hitTimerStart) / 1000000;
        if(hitTimerDiff < hitTargetTime) {
            hit = true;
        }else{
            hitTimerDiff = 0;
            hitTimerStart = 0;
            hit = false;
        }
    }

    @Override
    public void drawShapes(ShapeRenderer sr){
        if(!hit) {
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(rimColor);
            sr.circle(getX(), getY(), getR() + pad);
            sr.setColor(color);
            sr.circle(getX(), getY(), getR());
        } else {
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(new Color(178 / 255f, 181 / 255f, 186 / 255f, 1));
            sr.circle(getX(), getY(), getR() + pad);
            sr.setColor(Color.WHITE);
            sr.circle(getX(), getY(), getR());
        }
    }
    @Override
    public void init(){
        pad = 3;
        x = MathUtils.random(0, Main.WIDTH);
        y = Main.HEIGHT + r;
        angle = MathUtils.random(210, 330); //Select an angle from 7pi / 6 radians to 11pi / 6 radians
        rad = (float)Math.toRadians(angle);
        dx = (float)(Math.cos(rad) * speed);
        dy = (float)(Math.sin(rad) * speed);

        dead = false;
        slow = false;
        hit = false;

        hitTargetTime = 150;
        hitTimerStart = 0;
        hitTimerDiff = 0;
    }

    /**
     * enemyRanks - Enemies get created with Type and Rank
     *            as the Type increases, the Enemy has more health and size increases
     *            as the Rank increases, the Enemy increases size and size decreases
     */
    private void enemyRanks(){
        //Type 1 Enemies: weak, slow, and small
        if(type == 1){
            color = Color.BLUE;
            rimColor = new Color(8/255f,8/255f,200/255f,1);
            if(rank == 1){
                r = 5;
                speed = 1;
                health = 1;
            }
            if(rank == 2){
                r = 5;
                speed = 1;
                health = 2;
            }
        }
        //Type 2 Enemies: weak, fast, small
        if(type == 2){
            color = Color.GREEN;
            rimColor = new Color(4/255f,209/255f,17/255f,1);
            if(rank == 1){
                r = 4;
                speed = 2;
                health = 1;
            }
            if(rank == 2){
                r = 4;
                speed = 1;
                health = 2;
            }
        }
        //Type 3 Enemies: stronger, faster, and small
        if(type == 3){
            color = Color.BROWN;
            rimColor = new Color(76/255f,30/255f,0,1);
            if(rank == 1){
                r = 4;
                speed = 2;
                health = 2;
            }
            if(rank == 2){
                r = 5;
                speed = 2;
                health = 3;
            }
        }
        //Type 4 Enemies: stronger, slow, and bigger
        if(type == 4){
            color = Color.ORANGE;
            rimColor = new Color(204/255f, 102/255f, 0, 1);
            if(rank == 1){
                r = 8;
                speed = 5;
                health = 4;
            }
            if(rank == 2){
                r = 12;
                speed = 2;
                health = 10;
            }
        }
        //Type 5 enemies: designed to end the game
        if(type == 5){
            color = Color.RED;
            rimColor = new Color(153/255f, 0, 0, 1);
            if(rank == 1){
                r = 8;
                speed = 3;
                health = 7;
            }
            if(rank == 2){
                r = 20;
                speed = 2;
                health = 10;
            }
        }
    }

    /**
     * explode - this method deals with the explosion and repopulating of the Enemy.
     *         As the Type increases, so does the amount of enemies it splits into.
     * @param enemies - ArrayList<Enemy></Enemy> which will be modified for the explosions.
     *                this will essentially add the different types and ranks of enemies to this
     */
    public void explode(ArrayList<Enemy> enemies){
        int amount = 0;
        if(rank > 1){
            if(type == 1){
                amount = 3;
            }
            if(type == 2){
                amount = 3;
            }
            if(type == 3){
                amount = 4;
            }
            if(type == 4){
                amount = 6;
            }
            if(type == 5){
                amount = 8;
            }
        }
        for(int i = 0; i < amount; i++){
            Enemy e = new Enemy(getType(), getRank() - 1, false);
            e.x = this.x;
            e.y = this.y;
            double angle = Math.random() * 360;
            e.rad = (float)Math.toRadians(angle);
            e.dx = (float)Math.cos(e.rad) * speed;
            e.dy = (float)Math.sin(e.rad) * speed;
            enemies.add(e);
        }
    }

    /** hit - decrements health once a Player or Bullet hits the Enemy */
    public void hit(){
        health--;
        hitTimerStart = System.nanoTime();
        hit = true;
        if(health < 1){
            dead = true;
        }
    }
    /** @return x coordinate of the Enemy */
    public float getX(){return x;}
    /** @return y coordinate of the Enemy */
    public float getY(){return y;}
    /** @return Radius of the Enemy */
    public float getR(){return (float)r;}
    /** @return type of the Enemy */
    public int getType(){return type;}
    /** @return rank of the Enemy */
    public int getRank(){return rank;}

    /**
     * @return true if the Enemy is dead
     *         false if the Enemy is alive
     */
    public boolean isDead(){return dead;}

    /**
     * setSlow - setter method to decided whether or not to set the Enemy movements slow
     * @param b - if true, Enemy will move slowly. If false, Enemy will move normally
     */
    public void setSlow(boolean b){ slow = b; }
}

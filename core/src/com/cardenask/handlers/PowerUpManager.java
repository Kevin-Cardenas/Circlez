package com.cardenask.handlers;

/** PowerUpManager - class which handles the PowerUp system for the game */
public class PowerUpManager {

    private int level, index;

    /** PowerUpManager - constructor which instantiates the neccessary variables */
    public PowerUpManager(){
        level = 1;
        index = 0;
    }

    /** addPower - method which increments the power */
    public void addPower(){
        index++;
        //Making sure 5 circles are filled once you are level 3
        if(index > getLevel() * 2 && level < 4){
            index = 0;
            level++;
        }
        if(level > 3){
            level = 3;
            index = 6;
        }
    }
    /** @return int representation of the level */
    public int getLevel(){return level;}
    /** @return int representation of the index of current power */
    public int getIndex(){return index;}



}

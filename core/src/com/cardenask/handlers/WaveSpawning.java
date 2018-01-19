package com.cardenask.handlers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cardenask.entities.Enemy;
import com.cardenask.main.Main;

import java.util.ArrayList;

/** WaveSpawning - class which is responsible for dealing with the progression of the game and the amount of enemies on screen */
public class WaveSpawning {

    private int waveNumber;
    private float fade;
    private long waveStartTimer, waveStartTimerDiff;
    private boolean waveStart;
    private ArrayList<Enemy> enemies;

    /**
     * WaveSpawning - constructor which modifies the ArrayList<Enemy></Enemy> of enemies
     * @param enemies - reference copy which modifies enemies
     */
    public WaveSpawning(ArrayList<Enemy> enemies){
        init();
        this.enemies = enemies;
    }

    /**
     * drawWave - method which draws the text of the current round
     * @param sb - SpriteBatch which is responsible for the
     * @param font - the font which will draw the text
     */
    public void drawWave(SpriteBatch sb, BitmapFont font) {
        if (waveStartTimer != 0) {
            fade -= 1/3f * Gdx.graphics.getDeltaTime();
            GlyphLayout layout = new GlyphLayout(font, "- W A V E : " + waveNumber + " -");
            font.setColor(new Color(1, 1, 1, fade));
            font.draw(sb, "- W A V E : " + waveNumber + " -", Main.WIDTH / 2 - layout.width / 2, Main.HEIGHT / 2 - layout.height / 2);
        }
    }

    /** waveSystem - deals with the ending the round and the buffer between the rounds */
    public void waveSystem(){
        int waveDelay = 3000;
        if(waveStartTimer == 0 && enemies.size() == 0){
            waveNumber++;
            waveStart = false;
            waveStartTimer = System.nanoTime();
        }else{
            waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
            if(waveStartTimerDiff > waveDelay){
                waveStart = true;
                waveStartTimer = 0;
                waveStartTimerDiff = 0;
                fade = 1f;
            }
        }
        if(waveStart && enemies.size() == 0){
            createNewEnemies();
        }
    }

    /** createNewEnemies - deals with the type, rank, and amount of enemies per round. This is an infinite game */
    private void createNewEnemies(){
        int enemyCount, maxType, maxRank, type, rank;
        enemies.clear();
        if(waveNumber < 5){
            enemyCount = waveNumber * 2;
            maxType = 2;
            maxRank = 2;
        }
        else if(waveNumber < 7){
            enemyCount = waveNumber * 2;
            maxType = 3;
            maxRank = 2;
        }
        else if(waveNumber < 12){
            enemyCount = waveNumber * 3;
            maxType = 4;
            maxRank = 2;
        }
        else if(waveNumber < 15) {
            enemyCount = waveNumber * 4;
            maxType = 5;
            maxRank = 2;
        }else{
            enemyCount = waveNumber * 5;
            maxType = 5;
            maxRank = 2;
        }
        //Adding enemies on a per wave basis
        for(int i = 0; i < enemyCount; i++){
            type = 1 + (int)(Math.random() * maxType);
            rank = 1 + (int)(Math.random() * maxRank);
            enemies.add(new Enemy(type, rank, false));
        }
    }

    /** init - instantiates variables */
    public void init(){
        waveNumber = 0;
        waveStartTimer = 0;
        waveStartTimerDiff = 0;
        fade = 1f;
        waveStart = true;
    }

}

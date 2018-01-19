package com.cardenask.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Align;
import com.cardenask.entities.Bullet;
import com.cardenask.entities.Enemy;
import com.cardenask.entities.Player;
import com.cardenask.entities.PowerUp;
import com.cardenask.handlers.Buttons;
import com.cardenask.handlers.GameStateManager;
import com.cardenask.handlers.JoyStick;
import com.cardenask.handlers.PowerUpManager;
import com.cardenask.handlers.TextButtons;
import com.cardenask.handlers.WaveSpawning;
import com.cardenask.main.Main;
import com.cardenask.visuals.Explosion;
import com.cardenask.visuals.HUD;
import com.cardenask.visuals.SlowDown;

import java.util.ArrayList;

/**
 * PlayState - state responsible for handling the actual game play
 * @see com.cardenask.states.States
 */
public class PlayState extends States {

    private int score;
    private boolean paused, collectDraw;
    private long startTime, timeDiff, targetTime;
    private float step; //Alpha for the text

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<Explosion> explosion;
    private ArrayList<PowerUp> powerUps;

    private Touchpad touchPad;

    private Buttons buttons;
    private Button fireButton, pauseButton, playButton;

    private HUD hud;
    private PowerUpManager powerUpManager;
    private WaveSpawning waveSpawning;
    private SlowDown slowDown;

    private GlyphLayout layout, controlLayout, shootLayout, collectLayout;
    private String scoreText, controlText, shootText, collectText;
    private TextButtons textButtons;
    private TextButton back;

    /**
     * PlayState - constructor which instantiates the required variables
     * @param gsm - reference to the GameStateManager
     */
    public PlayState(GameStateManager gsm) {
        super(gsm);
        this.gsm = gsm;
        init();
    }

    @Override
    public void drawImages(SpriteBatch sb) {
        timeDiff = (System.nanoTime() - startTime) / 1000000;
        if (timeDiff < targetTime) {
            //Draw controls labels
            titleFont.setColor(new Color(1, 1, 1, step));
            titleFont.draw(sb, controlText, Main.WIDTH / 4 - controlLayout.width / 2, Main.HEIGHT / 16 - controlLayout.height / 2);
            //Draw fire labels
            titleFont.draw(sb, shootText, Main.WIDTH - Main.WIDTH / 4 - shootLayout.width / 2, Main.HEIGHT / 16 - shootLayout.height / 2);
            //Draw collection
            titleFont.setColor(new Color(1, 1, 1, step));
            titleFont.draw(sb, collectText, (Main.WIDTH - collectLayout.width) / 2, Main.HEIGHT - Main.HEIGHT / 4 - collectLayout.height / 2);
            collectDraw = true;
        }else{
            collectDraw = false;
        }

        if (slowDown.isSlowDown()) {
            //Clear Screen if we are slowed down
            Gdx.gl.glClearColor(66 / 255f, 131 / 255f, 229 / 255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        } else {
            //Clear Screen if we are not slowed down
            Gdx.gl.glClearColor(red, green, blue, alpha);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        //Draw High Score
        textFont.setColor(Color.WHITE);
        textFont.draw(sb, scoreText, Main.WIDTH - Main.WIDTH / 5 - layout.width / 2, Main.HEIGHT - Main.HEIGHT / 32 - layout.height / 2);
        //Draw Round
        waveSpawning.drawWave(sb, titleFont);
        }

    @Override
    public void drawShapes(ShapeRenderer sr) {
        stage.draw();
        hud.drawShapes(sr, player.getHealth(), powerUpManager.getIndex(), powerUpManager.getLevel());

        player.drawShapes(sr);
        drawCollect(sr);
        //Draw Bullets
        for(Bullet b : bullets) {
            b.drawShapes(sr);
        }
        //Draw Enemies
        for(Enemy e : enemies) {
            e.drawShapes(sr);
        }
        //Draw Explosions
        for(Explosion e : explosion) {
            e.drawShapes(sr);
        }
        //Draw PowerUps
        for(PowerUp p : powerUps) {
            p.drawShapes(sr);
        }
        //Draw SlowDown bar
        if (slowDown.isSlowDown()) {
            slowDown.drawShapes(sr);
        }
    }

    @Override
    public void update() {
        step -= 1 / 5f * Gdx.graphics.getDeltaTime();
        if (step < 0) step = 0;

        scoreText = "SCORE: " + score;
        layout = new GlyphLayout(titleFont, scoreText);
        if (paused) {
            back.setPosition(Main.WIDTH / 2, Main.HEIGHT / 2, Align.center);
            stage.addActor(back);
        }
        if (!paused) {
            back.remove();

            player.update();
            player.setDx(touchPad.getKnobPercentX() * 4);
            player.setDy(touchPad.getKnobPercentY() * 4);

            for(Enemy e : enemies) {
                e.update();
            }
            //Update Explosion
            for (int i = 0; i < explosion.size(); i++) {
                boolean remove = explosion.get(i).update();
                if (remove) {
                    explosion.remove(i);
                    i--;
                }
            }
            //Update Bullets
            for (int i = 0; i < bullets.size(); i++) {
                boolean remove = bullets.get(i).update();
                if (remove) {
                    bullets.remove(i);
                    i--;
                }
            }
            //Update PowerUps
            for (int i = 0; i < powerUps.size(); i++) {
                boolean remove = powerUps.get(i).update();
                if (remove) {
                    powerUps.remove(i);
                    i--;
                }
            }
        /* Collisions */
            bulletEnemyCollision();
            playerEnemyCollision();
            playerPowerUpCollision();
            waveSpawning.waveSystem();
            slowDown.update(enemies);
            //Check to see if we should end game
            if (player.isDead()) {
                gsm.setState(new GameOverState(gsm, score));
            }
        }
        stage.act(Gdx.graphics.getDeltaTime());
    }

    /* Collision Detections */

    /**
     * playerEnemyCollision - this collision deals with the Player colliding with an Enemy
     *                      collision is calculated by using the Pythagorean Theorem. So,
     *                      when the hypotenuse between the two entities is less then the
     *                      combined sizes of the two radii the Player and Enemy have collided.
     */
    private void playerEnemyCollision() {
        if (!player.isRecovery()) {
            float px = player.getX();
            float py = player.getY();
            float pr = player.getR();
            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);
                float ex = e.getX();
                float ey = e.getY();
                float er = e.getR();
                //Pythagorean Theorem Variables
                float dx = px - ex;
                float dy = py - ey;
                float dr = pr + er;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);//Hypotenuse
                if (dist < dr - 3) {
                    player.hit();
                    e.hit();
                    explosion.add(new Explosion(player.getX(), player.getY(), player.getR(), true));
                    if (!TitleState.MUTE_SOUND) {
                        res.playSound("Explode");
                    }
                }
                if (e.isDead()) {
                    enemies.remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * bulletEnemyCollision - this collision deals with a Bullet colliding with an Enemy
     *                      collision is calculated by using the Pythagorean Theorem. So,
     *                      when the hypotenuse between the two entities is less then the
     *                      combined sizes of the two radii the Bullet and Enemy have collided.
     */
    private void bulletEnemyCollision() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            float bx = b.getX();
            float by = b.getY();
            float br = b.getR();
            for (int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                float ex = e.getX();
                float ey = e.getY();
                float er = e.getR();
                //Pythagorean Theorem Collision
                float dx = bx - ex;
                float dy = by - ey;
                float dr = br + er;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist < dr) {
                    e.hit();
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }
        //Separate Loop to check if an enemy is dead or not
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.isDead()) {
                /* Drops */
                int chance = MathUtils.random(1, 100);
                //Chance for Extra Life
                if (chance < 3 )
                    powerUps.add(new PowerUp(3, e.getX(), e.getY()));
                //Chance for Slow Down
                else if (chance < 13)
                    powerUps.add(new PowerUp(2, e.getX(), e.getY()));
                //Chance for PowerUp Drop
                else if (chance < 32)
                    powerUps.add(new PowerUp(1, e.getX(), e.getY()));

                explosion.add(new Explosion(e.getX(), e.getY(), e.getR(), false));
                score += (e.getType() + e.getRank());
                //Explode
                e.explode(enemies);
                enemies.remove(i);
                i--;
            }
        }
    }

    /**
     * playerPowerUpCollision - this collision deals with the Player colliding with a PowerUp
     *                      collision is calculated by using the Pythagorean Theorem. So,
     *                      when the hypotenuse between the two entities is less then the
     *                      combined sizes of the two radii the Player and PowerUp have collided.
     */
    private void playerPowerUpCollision() {
        float px = player.getX();
        float py = player.getY();
        float pr = player.getR();
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            float tx = powerUp.getX();
            float ty = powerUp.getY();
            float tr = powerUp.getR();
            //Pythagorean Theorem Variables
            float dx = px - tx;
            float dy = py - ty;
            float dr = pr + tr;
            float dist = (float) (Math.sqrt(dx * dx + dy * dy));
            if (dist < dr - 4) {
                if (powerUp.getType() == 1) {
                    powerUpManager.addPower();
                } else if (powerUp.getType() == 2) {
                    slowDown.slowStart();
                } else {
                    player.addLife();
                    if (!TitleState.MUTE_SOUND) {
                        res.playSound("Life");
                    }
                }
                powerUps.remove(i);
                i--;
            }
        }
    }

    @Override
    public void handleInput() {
        if (fireButton.isPressed()) {
            player.fireBullet(bullets, powerUpManager.getLevel());
        }
        if (!paused) {
            if (!TitleState.MUTE_SOUND && !res.isPlaying()) {
                res.setPlaying(true);
                res.startMusic("Main", true);
            }
            stage.addActor(pauseButton);
            playButton.remove();
        }
        if (paused) {
            if (!TitleState.MUTE_SOUND) {
                res.setPlaying(false);
                res.pauseMusic("Main");
            }
            stage.addActor(playButton);
            pauseButton.remove();
        }
        if (pauseButton.isPressed() && !slowDown.isSlowDown()) {
            paused = true;
            if (!TitleState.MUTE_SOUND) {
                res.playSound("Select");
            }
        }
        if (playButton.isPressed()) {
            paused = false;
            if (!TitleState.MUTE_SOUND) {
                res.playSound("Select");
            }
        }
        if (back != null && back.isPressed()) {
            gsm.setState(new TitleState(gsm));
        }
    }

    /**
     * drawCollect - this helper method draws the mini tutorial describing the various power ups in the game
     * @param sr - ShapeRenderer responsible for drawing the power ups
     */
    private void drawCollect(ShapeRenderer sr){
        //Draw Collect
        if(collectDraw) {
            Color color;
            //Drawing Sample PowerUps
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    color = new Color(1.0f, 11 / 255f, 252 / 255f, step);
                }
                else if (i == 1) {
                    color = new Color(0f, 1.0f, 1.0f, step);
                }else {
                    color = new Color(1.0f, 1.0f, 1.0f, step);
                }
                sr.set(ShapeRenderer.ShapeType.Filled);
                sr.setColor(color);
                sr.rect(Main.WIDTH / 2 - Main.WIDTH / 14 + i * 40, Main.HEIGHT - Main.HEIGHT / 3, 16, 16);
            }
        }
    }

    @Override
    public void init() {
        player = new Player();

        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        explosion = new ArrayList<Explosion>();
        powerUps = new ArrayList<PowerUp>();

        JoyStick joyStick = new JoyStick();
        touchPad = joyStick.getTouchPad();

        score = 0;

        /* BUTTON STUFF */
        buttons = new Buttons();

        fireButton = buttons.createButton("Images/JoyStick.png", "Fire_Button");
        fireButton.setPosition(Main.WIDTH - Main.WIDTH / 3, Main.HEIGHT / 12 - 8);

        pauseButton = buttons.createButton("Images/Pause.png", "Pause_Button");
        pauseButton.setSize(50, 50);
        pauseButton.setPosition(Main.WIDTH / 2 - pauseButton.getWidth() / 2, Main.HEIGHT / 8 - pauseButton.getHeight() / 2);

        playButton = buttons.createButton("Images/Play.png", "Play_Button");
        playButton.setSize(50, 50);
        playButton.setPosition(Main.WIDTH / 2 - playButton.getWidth() / 2, Main.HEIGHT / 8 - playButton.getHeight() / 2);

        hud = new HUD(30, Main.HEIGHT - Main.HEIGHT / 30, player.getR());
        powerUpManager = new PowerUpManager();
        waveSpawning = new WaveSpawning(enemies);
        slowDown = new SlowDown();
        textButtons = new TextButtons(titleFont);
        paused = false;
        back = textButtons.backCreation("MAIN MENU?", "Images/Text_Button_Up.png");

        stage.addActor(touchPad);
        stage.addActor(fireButton);

        res.loadSound("Sound/extralife.ogg", "Life");
        res.loadSound("Sound/explode.ogg", "Explode");
        res.loadSound("Sound/select.wav", "Select");

        if (!TitleState.MUTE_SOUND) {
            res.loadMusic("Music/Main.mp3", "Main");
            res.setPlaying(true);
            res.startMusic("Main", true);
            res.changeVolume("Main", 0.4f);
        }

        startTime = System.nanoTime();
        timeDiff = 0;
        targetTime = 5000;
        step = 1.0f;
        shootText = "  FIRE";
        controlText = "MOVEMENT";
        collectText = "WHEN POSSIBLE EAT US!";
        controlLayout = new GlyphLayout(titleFont, controlText);
        shootLayout = new GlyphLayout(titleFont, shootText);
        collectLayout = new GlyphLayout(titleFont, collectText);
        collectDraw = true;
    }

    @Override
    public void dispose() {
        stage.dispose();
        buttons.dispose();
        textButtons.dispose();
        if (!TitleState.MUTE_SOUND) {
            res.disposeSound("Life");
            res.disposeSound("Explode");
            res.disposeSound("Select");
            res.stopMusic("Main", true);
        }
    }
}

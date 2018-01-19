package com.cardenask.handlers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/** ResourceManager - class which handles the resources for Music and Sound */
public class ResourceManager {

    private HashMap<String, Music> musicMap;
    private HashMap<String, Sound> soundMap;
    private boolean playing; //For whether or not the Music is playing

    /** ResourceManager - constructor which instantiates neccessary variables */
    public ResourceManager() {
        musicMap = new HashMap<String, Music>();
        soundMap = new HashMap<String, Sound>();
        playing = false;
    }


    /**
     * loadMusic - loads the Music into the HashMap<String, Music></String, Music>
     * @param path - String representation of the Music file location
     * @param key - String representation of the key for the Music in the HashMap<String, Music></String, Music>
     */
    public void loadMusic(String path, String key) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        musicMap.put(key, music);
    }

    /**
     * startMusic - starts playing the Music based on the given key
     * @param key - String representation for the given key for the specific Music file that needs to be played
     * @param looping - if true, the Music will loop. If false, the Music will play through only once
     */
    public void startMusic(String key, boolean looping){
        if(isPlaying()) {
            musicMap.get(key).play();
            musicMap.get(key).setLooping(looping);
        }
    }

    /**
     * stopMusic - combination of stopping the Music and disposing
     * @param key - String representation for the given key for the specific Music file that needs to be stopped
     * @param dispose - if true, the Music file will be stopped AND the file will be disposed.
     *                If false, the Music file will stop playing, but will not be disposed
     */
    public void stopMusic(String key, boolean dispose){
        setPlaying(false);
        Music m = musicMap.get(key);
        if(m != null) {
            m.stop();
            if(dispose) disposeMusic(key);
        }
    }

    /**
     * pauseMusic - pauses the current Music
     * @param key - String representation for the given key for the specific Music file that needs to be paused
     */
    public void pauseMusic(String key){
        musicMap.get(key).pause();
    }

    /**
     * changeVolume - adjusts the volume by using a scalar of the Music playing
     * @param key - String representation for the given key for the specific Music file that needs the volume adjusted
     * @param volume - a float, between 0.0f - 1.0f, to scale the volume down
     */
    public void changeVolume(String key, float volume){
        musicMap.get(key).setVolume(volume);
    }

    /**
     * disposeMusic - disposes Music even if it is not paused
     * @param key - String representation for the given key for the specific Music file that needs to get disposed
     */
    public void disposeMusic(String key){
        if(musicMap.get(key) != null) {
            musicMap.get(key).dispose();
        }
    }

    /**
     * loadSound - loads the given Sound into memory
     * @param path - String representation of the file location of the Sound file
     * @param key - String representation of the Sound name
     */
    public void loadSound(String path, String key){
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        soundMap.put(key, sound);
    }

    /**
     * playSound - plays a Sound based on the given key
     * @param key - String representation of the Sound name
     */
    public void playSound(String key){
        soundMap.get(key).play();
    }

    /**
     * disposeSound - disposes the Sound based on the key name
     * @param key - String representation of the Sound to be disposed
     */
    public void disposeSound(String key){
        if(soundMap.get(key) != null) {
            soundMap.get(key).dispose();
        }
    }

    /** @return true if Music is playing. Return false if it is paused */
    public boolean isPlaying(){ return playing; }

    /**
     * setPlaying - setter which sets the Music to be playing or paused
     * @param b - if true, the Music will play. If false, the Music will pause
     */
    public void setPlaying(boolean b){ playing = b; }

}

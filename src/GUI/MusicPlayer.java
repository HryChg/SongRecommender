package GUI;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.Playlist;
import model.Song;

import java.util.ArrayList;

public class MusicPlayer {
    private MediaPlayer mp;
    private Playlist playlist;
    private ArrayList<Media> actualMP3s;


    public MusicPlayer(){
        playlist = new Playlist("CurrentPlaylist");
        actualMP3s = new ArrayList<>();
    }


    //EFFECTS: play a playlist
    public void select(Playlist playlist){
        this.playlist = playlist;
        //todo convert these playlist to actual MP3 files

    }

    //EFFECTS: play a song
    public void select(Song song){
        //todo convert these song to actual MP3 files and add it to ActualMP3Files
    }


    public void play(){
        for (Media media: actualMP3s){
            mp = new MediaPlayer(media);
        }
    }

    public void pause(){
        mp.pause();
    }

    public void stop(){
        mp.stop();
    }

    public void skip(){
        //TODO. Skip a song
    }


    public void restart(){
        mp.stop();
        play();
    }




}

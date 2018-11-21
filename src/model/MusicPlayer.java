package model;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javazoom.jl.decoder.JavaLayerException;
import model.Playlist;
import model.Song;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MusicPlayer {
    private InternalPlayer internalPlayer;

    private ArrayList<FileInputStream> actualMP3s;


    public MusicPlayer(Playlist playlist){
        actualMP3s = new ArrayList<>();
        for(Song song: playlist.getListOfSongs()){
            String songName = song.getSongName();
            try {
                FileInputStream fis = new FileInputStream("songs/" + songName + ".mp3");
                actualMP3s.add(fis);
            } catch (FileNotFoundException e){
                System.out.println(songName+": not found under folder \"songs\"");
            }
        }
    }


    //EFFECTS: replace with a new playlist
    public void select(Playlist playlist){

    }

    //EFFECTS: play a song
    public void select(Song song){

    }


    public void play(){
        //update
        for (FileInputStream fis: actualMP3s){
            try {
                internalPlayer = new InternalPlayer(fis);
                internalPlayer.play();

            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
        //remove the song from the actualMP3 files once updated with the notification from itnernal player

    }

    public void pause(){

    }

    public void stop(){


    }

    public void skip(){
        //TODO. Skip a song
    }


    public void restart(){

    }

    //testing out
    public static void main(String[] args) {
        try{
            Playlist p = new Playlist("p");
            p.readFromFile("savedFiles/savedPlaylists/MainPlaylist.txt");

            MusicPlayer musicPlayer = new MusicPlayer(p);
            musicPlayer.play();


        } catch (Exception e){
            throw new RuntimeException();
        }
    }


}

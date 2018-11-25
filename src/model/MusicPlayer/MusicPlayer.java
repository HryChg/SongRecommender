package model.MusicPlayer;


import javazoom.jl.decoder.JavaLayerException;
import model.Playlist;
import model.PlaylistManager;
import model.Song;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class MusicPlayer {
    private static MusicPlayer firstInstance = null; //Singleton Pattern


    private InternalPlayer internalPlayer;
    private ArrayList<FileInputStream> actualMP3s;
    private Playlist currentPlaylist = new Playlist("MusicPlayer Playlist");
    private Thread t;
    private boolean skipSong = false;
    private boolean stopPlayer = false;

    //Singleton Pattern
    private MusicPlayer(){}
    public static MusicPlayer getInstance(){
        if (firstInstance == null){
            firstInstance = new MusicPlayer();
        }
        return firstInstance;
    }

    public static MusicPlayer refreshInstance(){
        firstInstance = null;
        return firstInstance = new MusicPlayer();
    }

    public void setSong(Song song) {
        setPlayedTimeAndLastPlayedDate(song);
        actualMP3s = new ArrayList<>();
        addSongToActualMP3s(song);
    }

    public void setPlaylist(Playlist playlist) {
        for (Song song: playlist.getListOfSongs()){
            currentPlaylist.addSong(song);
        }

        //currentPlaylist = playlist;
        //loading playlistSong to actualMP3
        actualMP3s = new ArrayList<>();
        for (Song song : playlist.getListOfSongs()) {
            addSongToActualMP3s(song);
        }
    }

    public Playlist getPlaylist(){
        return currentPlaylist;
    }

    private void setPlayedTimeAndLastPlayedDate(Song song) {
        song.setPlayedTime(new Timestamp(new Date().getTime()));
        song.setLastPlayedDate(new Timestamp(new Date().getTime()));
    }

    private void addSongToActualMP3s(Song song) {
        String songName = song.getSongName();
        try {
            FileInputStream fis = new FileInputStream("songs/" + songName + ".mp3");
            actualMP3s.add(fis);
        } catch (FileNotFoundException e) {
            System.out.println(songName + ": not found under folder \"songs\"");
        }
    }

    public void initializeThreadAndPlay() {

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Music Player initialized.....");
                    playThread(actualMP3s);
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        };
        t = new Thread(r);
        t.start();

        //print out thread name based the seocond it was created
        Long name = new Date().getTime();
        t.setName(Long.toString(name));
        System.out.println("\nCurrent Thread Name: " + t.getName() + "\n");
    }

    private void playThread(ArrayList<FileInputStream> actualMP3s) throws JavaLayerException {
        //looping through actualMP3s and create new internalPlayer to for every MP3
        int index = 0;

        outerloop:
        for (FileInputStream fis : actualMP3s) {
            Song song = currentPlaylist.getSong(index);
            setPlayedTimeAndLastPlayedDate(song);
            internalPlayer = new InternalPlayer(fis);
            internalPlayer.play();


            while (true) {

                if (internalPlayer.isComplete() || skipSong) {
                    internalPlayer.close();
                    skipSong = false;
                    //move on to the next song
                    break;
                }

                if (stopPlayer) {
                    internalPlayer.close();
                    stopPlayer = false;
                    break outerloop;
                }
            }
        }
    }

    public void resume() {
        System.out.println("Music Player resumed.....");
        try {
            internalPlayer.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        System.out.println("Music Player paused.....");
        internalPlayer.pause();

    }

    public void stop() {
        System.out.println("Music Player stopped.....");
        stopPlayer = true;
    }

    public void skip() {
        System.out.println("Music Player skip to next song.....");
        skipSong = true;
    }


//    //testing out
//    public static void main(String[] args) {
//        PlaylistManager pm = new PlaylistManager();
//
//        Playlist playlist = new Playlist("TestingDeleteThisWhenYouCan");
//        pm.saveMultipleAudioFilesToPlaylist("/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/songs", playlist);
//        playlist.print();
//
//        //loading the playlist to music player
//        MusicPlayer musicPlayer = MusicPlayer.getInstance();
//        musicPlayer.setPlaylist(playlist);
//
//
//
//        try {
//            musicPlayer.initializeThreadAndPlay();
//
//            Thread.sleep(5000);
//            musicPlayer.pause();
//
//            Thread.sleep(5000);
//            musicPlayer.resume();
//
//            Thread.sleep(5000);
//            musicPlayer.skip();
//
//            Thread.sleep(5000);
//            musicPlayer.stop();
//
//
//            //after stop the music. Refresh the instance to get it playing again
//            musicPlayer = MusicPlayer.refreshInstance();
//            musicPlayer.setPlaylist(playlist);
//            Thread.sleep(5000);
//            musicPlayer.initializeThreadAndPlay();
//
//
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }


}


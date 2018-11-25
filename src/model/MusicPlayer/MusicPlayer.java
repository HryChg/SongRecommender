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
    private InternalPlayer internalPlayer;
    private ArrayList<FileInputStream> actualMP3s;
    private Playlist currentPlaylist;
    private Thread t;
    private boolean skipSong = false;
    private boolean stopPlayer = false;
    private boolean pausePlayer = false;

    public MusicPlayer(Song song) {
        setPlayedTimeAndLastPlayedDate(song);

        actualMP3s = new ArrayList<>();
        addSongToActualMP3s(song);
    }

    private void setPlayedTimeAndLastPlayedDate(Song song) {
        song.setPlayedTime(new Timestamp(new Date().getTime()));
        song.setLastPlayedDate(new Timestamp(new Date().getTime()));
    }

    public MusicPlayer(Playlist playlist) {
        currentPlaylist = playlist;

        //loading playlistSong to actualMP3
        actualMP3s = new ArrayList<>();
        for (Song song : playlist.getListOfSongs()) {
            addSongToActualMP3s(song);
        }
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
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    playThread(actualMP3s);
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        };
        t = new Thread(r);
        t.start();

        //print out thread name based the seocond it was created
//        Long name = new Date().getTime();
//        t.setName(Long.toString(name));
//        System.out.println("\nCurrent Thread Name: " + t.getName() + "\n");
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


    //testing out
    public static void main(String[] args) {
        PlaylistManager pm = new PlaylistManager();

        Playlist playlist = new Playlist("TestingDeleteThisWhenYouCan");
        pm.saveMultipleAudioFilesToPlaylist("/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/songs", playlist);
        playlist.print();

        //loading the playlist to music player
        MusicPlayer musicPlayer = new MusicPlayer(playlist);

        try {
            musicPlayer.initializeThreadAndPlay();

            Thread.sleep(5000);
            musicPlayer.pause();

            Thread.sleep(5000);
            musicPlayer.resume();

            Thread.sleep(5000);
            musicPlayer.skip();

            Thread.sleep(5000);
            musicPlayer.stop();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}


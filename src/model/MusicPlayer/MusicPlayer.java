package model.MusicPlayer;


import javazoom.jl.decoder.JavaLayerException;
import model.Playlist;
import model.Song;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MusicPlayer {
    private InternalPlayer internalPlayer;
    private ArrayList<FileInputStream> actualMP3s;

    private Thread t;


    public MusicPlayer(Playlist playlist) {
        //loading playlistSong to actualMP3
        actualMP3s = new ArrayList<>();
        for (Song song : playlist.getListOfSongs()) {
            String songName = song.getSongName();
            try {
                FileInputStream fis = new FileInputStream("songs/" + songName + ".mp3");
                actualMP3s.add(fis);
            } catch (FileNotFoundException e) {
                System.out.println(songName + ": not found under folder \"songs\"");
            }
        }
    }


    public void play() {
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    playThread(actualMP3s);
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();

    }

    private void playThread(ArrayList<FileInputStream> actualMP3s) throws JavaLayerException {
        //looping thru actualMP3s and create new internalPlayer to for every MP3
        for (FileInputStream fis : actualMP3s) {
            internalPlayer = new InternalPlayer(fis);
            internalPlayer.play();

            //pause for loop until the single song is done playing
            Boolean pauseOnForLoop = true;
            while (pauseOnForLoop) {
                if (internalPlayer.isComplete()) {
                    pauseOnForLoop = false;
                }

            }
        }
    }


    public void pause() {
        internalPlayer.pause();

    }

    public void stop() {
        //todo close the entire player


    }

    public void skip() {
        //TODO. Skip a song

        internalPlayer.close();
        try {
            internalPlayer.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }

    }


    public void restart() {
        //TODO: restart the playlist
    }

    //testing out
    public static void main(String[] args) {
        try {
            Playlist p = new Playlist("p");
            p.readFromFile("savedFiles/savedPlaylists/MainPlaylist.txt");

            MusicPlayer musicPlayer = new MusicPlayer(p);
            musicPlayer.play();

            //this doesnt work, as musicPLayer.play is running before pause. Not simultaneously.
            Thread.sleep(5000);
            musicPlayer.pause();

            Thread.sleep(5000);
            musicPlayer.play();

            //TODO stop is not working!!!!!!
            Thread.sleep(5000);
            musicPlayer.skip();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

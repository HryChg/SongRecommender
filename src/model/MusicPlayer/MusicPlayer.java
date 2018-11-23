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
    private boolean skipSong = false;





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
    }

    private void playThread(ArrayList<FileInputStream> actualMP3s) throws JavaLayerException {
        //looping thru actualMP3s and create new internalPlayer to for every MP3
        for (FileInputStream fis : actualMP3s) {
            internalPlayer = new InternalPlayer(fis);
            internalPlayer.play();




            while (true) {
                if (internalPlayer.isComplete() || skipSong) {
                    internalPlayer.close();
                    skipSong = false;
                    //move on to the next song
                    break;
                }

            }
        }
    }


    public void pause() {
        internalPlayer.pause();
    }

    public void stop() { internalPlayer.stop();}

    public void skip() {
        skipSong = true;
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

//            Thread.sleep(5000);
//            musicPlayer.pause();
//
//            Thread.sleep(5000);
//            musicPlayer.play();

            Thread.sleep(5000);
            //TODO skip is not working
            musicPlayer.skip();
            //musicPlayer.stop();

            
            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

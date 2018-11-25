package model.MusicPlayer;


import exceptions.EmptyPlaylistException;
import javazoom.jl.decoder.JavaLayerException;
import model.Playlist;
import model.Song;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.System.exit;

public class MusicPlayer {
    private InternalPlayer internalPlayer;
    private ArrayList<FileInputStream> actualMP3s;
    private Playlist currentPlaylist;
    private Thread t;
    private boolean skipSong = false;
    private boolean stopPlayer = false;

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
        //looping through actualMP3s and create new internalPlayer to for every MP3
        int index = 0;
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
                    return;
                }
            }
        }
    }


    public void pause() {
        internalPlayer.pause();
    }

    public void stop() {
        stopPlayer = true;
    }

    public void skip() {
        skipSong = true;
    }

    public void restart() {
        //todo find a way to restart the player

    }

    //testing out
    public static void main(String[] args) {

        Playlist p = new Playlist("p");
        p.readFromFile("savedFiles/savedPlaylists/MainPlaylist.txt");

        MusicPlayer musicPlayer = new MusicPlayer(p);
        musicPlayer.play();


//            //play single song
//            Song song9 = p.getSong(9);
//            MusicPlayer musicPlayer = new MusicPlayer(song9);
//            musicPlayer.play();


        System.out.println("start testing");


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        System.out.println("pause the player");
                        musicPlayer.pause();
                    }
                },
                5000
        );

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("play the player again");
                        musicPlayer.play();
                    }
                },
                15000
        );

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("skip a song");
                        musicPlayer.skip();
                    }
                },
                25000
        );




        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("stop the player");
                        musicPlayer.stop();
                    }
                },
                35000
        );

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("saving to data base");
                            p.writeToFile(p.convertToGsonString());



                            //force stop java virtual machine!!!
                            exit(0);




                        } catch (EmptyPlaylistException e) {
                            e.printStackTrace();
                        }
                    }
                },
                45000
        );

//        System.out.println("saving to data base");
//        try {
//            p.writeToFile(p.convertToGsonString());
//        } catch (EmptyPlaylistException e) {
//            e.printStackTrace();
//        }




    }


}

package model;

import exceptions.AlreadyInPlaylistException;
import exceptions.EmptyPlaylistException;
import exceptions.NotAudioFileException;
import javafx.print.PageLayout;
import org.apache.poi.ss.formula.functions.Today;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class PlaylistManager {
    private AudioParser ap;
    private Timestamp currentTimeStamp;
    private Scanner scanner;
    private Double LIMITS_FOR_RECENTLY_PLAYED = 1.728e+8; //two days
    private Double LIMITS_FOR_LOST_SONGS = 2.592e+8; //three days

    public PlaylistManager() {

        currentTimeStamp = new Timestamp(0);
        scanner = new Scanner(System.in);
    }


    //MODIFIES: playlist
    //EFFECTS: open multiple audio files under specified directory, convert each AudioFiles to Song and Add to Playlist.
    //     skip files that are not of audio format or are already in playlist.
    //     Throw NullPointerException if filesLocation not exists.
    public void saveMultipleAudioFilesToPlaylist(String filesLocation, Playlist playlist) throws NullPointerException {
        File dir = new File(filesLocation);

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                try {
                    ap = new AudioParser(); //remember to always create a new parser for each new file!
                    Song parsedSong = ap.parseFileToSong(file);

                    if (playlist.contains(parsedSong)) {
                        System.out.println(file.getName() + " is already in playlist.");
                        System.out.println("Skipping to the next file... \n");
                    }

                    playlist.addSong(parsedSong);
                } catch (NotAudioFileException e) {
                    System.out.println("Detected a file that is not audio: " + file.getName());
                    System.out.println("Skipping to the next file... \n");
                }
            }

        } else {
            throw new NullPointerException();
        }

        System.out.println("Audio Files transfer to playlist: " + playlist.getPlayListName() + " completed. \n");

    }


    // MODIFIES: playlist
    // EFFECTS: ask user if first time playing a song or recently played it.
    public void askEachSongStatus(Playlist playlist) {
        currentTimeStamp.setTime(new Date().getTime());
        int countSongUpdated = 0;

        for (int i = 0; i <= (playlist.getSize() - 1); i++) {
            Song currentSong = playlist.getSong(i);

            boolean userReply = false;
            while (!userReply) {
                System.out.println("Current Song is " + currentSong.getSongName());
                System.out.println("Is this song played for the first time or recently played?");
                System.out.println("[a] played for the first time");
                System.out.println("[b] recently played");
                System.out.println("[c] Skip to the next song");
                String answer = scanner.nextLine();

                if (answer.equals("a")) {
                    currentSong.setLastPlayedDate(currentTimeStamp);
                    currentSong.setPlayedTime(currentTimeStamp);
                    countSongUpdated++;
                    userReply = true;
                } else if (answer.equals("b")) {
                    currentSong.setLastPlayedDate(currentTimeStamp);
                    userReply = true;
                    countSongUpdated++;
                } else if (answer.equals("c")) {
                    userReply = true;
                } else {
                    System.out.println("Wrong input, Please try again.");
                }
            }
        }

        System.out.println("Number of Songs Updated: " + countSongUpdated);

    }


    //MODIFIES: database
    //EFFETCS: update dataBase with new mp3 files
    //TODO: the mp3 files I thrown in has no metadata
    public void addFilesToDatabase(String databasePath, String newFilePath) {
        try {
            Playlist database = new Playlist("Song Database");
            database.readFromFile(databasePath);
            saveMultipleAudioFilesToPlaylist(newFilePath, database);
            database.writeToFile(database.convertToGsonString());
        } catch (EmptyPlaylistException e) {
            e.printStackTrace();
        }
    }


    public Playlist filter(Playlist datbase, Boolean favorite, Boolean hate, Boolean recentlyPlayed, Boolean lostSong, Boolean neverPlayed){
        String outputPlaylistName = "Filtered Playlist----";
        if(favorite){
            outputPlaylistName += "Favorite, ";
        }
        if(hate){
            outputPlaylistName += "Hate, ";
        }
        if(recentlyPlayed){
            outputPlaylistName += "Recently Played, ";
        }
        if(lostSong){
            outputPlaylistName += "Lost Song, ";
        }
        if(neverPlayed){
            outputPlaylistName += "Never Played, ";
        }

        Playlist outputPlaylist = new Playlist(outputPlaylistName);
        for (Song song: datbase.getListOfSongs()){
            //--------------------------------------------------------------------------------------------------
            if (favorite){
                if (song.getIsFavorite()){
                    outputPlaylist.addSong(song);
                }
            }

            //--------------------------------------------------------------------------------------------------
            if(hate){
                if(song.getIsHate()){
                    outputPlaylist.addSong(song);
                }
            }

            //--------------------------------------------------------------------------------------------------
            if(recentlyPlayed) {
                //songs that way played in the last 24 hrs
                Timestamp timeStampForToday = new Timestamp(new Date().getTime());
                Long songPlayedDate = song.getLastPlayedDate().getTime();
                if (songPlayedDate.equals(null)) {
                    //note some songs are just null
                    //do nothing
                }
                if (timeStampForToday.getTime() - LIMITS_FOR_RECENTLY_PLAYED < song.getLastPlayedDate().getTime()) {
                    outputPlaylist.addSong(song);
                }
            }


            //--------------------------------------------------------------------------------------------------
            if(lostSong){
                //song that has not been played for 2 days
                Timestamp timeStampForToday = new Timestamp(new Date().getTime());
                Long songPlayedDate = song.getLastPlayedDate().getTime();
                if (songPlayedDate.equals(null)) {
                    //note some songs are just null
                    //do nothing
                }
                if (timeStampForToday.getTime() - LIMITS_FOR_LOST_SONGS > song.getLastPlayedDate().getTime()) {
                    outputPlaylist.addSong(song);
                }
            }

            if(neverPlayed){
                //Long songPlayedDate = song.getLastPlayedDate().getTime();
                if (song.getLastPlayedDate() == null) {
                    outputPlaylist.addSong(song);
                }
            }
        }

        return outputPlaylist;
    }




    private Playlist filter2(Playlist playlist, String songType) {
        Playlist p = new Playlist("");



        if (songType.equals("favorite")) {
            for (Song song : playlist.getListOfSongs()) {
                if (song.getIsFavorite()) {
                    p.addSong(song);
                }
            }
        }

        if (songType.equals("hate")) {
            for (Song song : playlist.getListOfSongs()) {
                if (song.getIsHate()) {
                    p.addSong(song);
                }
            }
        }

        if (songType.equals("recentlyPlayed")) {
            Timestamp todayTimeStamp = new Timestamp(new Date().getTime());
            for (Song song : playlist.getListOfSongs()) {
                //if the songs is played just in the last three days
                Long songPlayedDate = song.getLastPlayedDate().getTime();
                if ( songPlayedDate.equals(null)){
                    //do nothing
                }
                if (todayTimeStamp.getTime() - 8.64e+7 < song.getLastPlayedDate().getTime()) {
                    p.addSong(song);
                }
            }
        }

        return p;


    }


    public static void main(String[] args) {
        PlaylistManager pm = new PlaylistManager();

        Playlist database = new Playlist("database");
        database.readFromFile("savedFiles/savedPlaylists/MainPlaylist.txt");

//        //filter database and output playlist
//        Playlist outputPlaylist = pm.filter(database, false, false, false, false, true);
//        outputPlaylist.print();



//        Song song = database.getSong(5);
//        Long today = new Date().getTime();
//        System.out.println(today);
//        System.out.println(86400000);
//        System.out.println(today-86400000);
//        System.out.println(song.getPlayedTime().getTime());

//        Song s = new Song("s");
//        System.out.println(s.getLastPlayedDate());


        String path = "/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/songs";
        pm.saveMultipleAudioFilesToPlaylist(path, database);
        database.print();




    }


}

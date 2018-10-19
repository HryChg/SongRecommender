package ui;

import com.google.gson.Gson;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import exceptions.AlreadyInPlaylistException;
import exceptions.EmptyPlaylistException;
import exceptions.EmptyStringException;
import exceptions.NotAudioFileException;
import model.AudioParser;
import model.Playlist;
import model.Song;


import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;


public class Main {
    private static Playlist mainPlaylist = new Playlist("MainPlaylist");
    private static String audioFilesLocation = "songs";

    private Scanner scanner = new Scanner(System.in);
    private Timestamp currentTimeStamp = new Timestamp(0);
    private Gson gson = new Gson();



    // MODIFIES: this, scanner
    // EFFECTS: prompt user to input songs into a mainPlaylist
    public void inputSongsToPlaylist() {
        try {
            mainPlaylist.setPlaylistName("uiPlaylist");
        } catch (EmptyStringException e) {
            System.out.println("setPlaylistName detects an emptyString for playlistName");
        }

        // asking user to input songs
        boolean stillExecute = true;
        while (stillExecute) {
            System.out.println("Please input a song name: (enter q to quit)");
            String name = scanner.nextLine();

            if (!name.equals("q")) {
                Song curr_song = new Song("test");

                try {
                    curr_song.setSongName(name);
                    mainPlaylist.addSong(curr_song);
                } catch (AlreadyInPlaylistException e){
                    System.out.println("Fail: curr_song is Null!");
                } catch (EmptyStringException e){
                    System.out.println("Fail: Current Song Name is null.");
                }
                System.out.println("The songs <" + name + "> has been added");
            } else {
                mainPlaylist.print();
                stillExecute = false;
            }


        }
    }


    // MODIFIES: this
    //EFFECTS: ask user if first time playing a song or recently played it.
    public void askEachSongStatus() {
        currentTimeStamp.setTime(new Date().getTime());
        int countSongUpdated = 0;

        for (int i = 0; i <= (mainPlaylist.getSize() - 1); i++) {
            Song currentSong = mainPlaylist.getSong(i);

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



    //MODIFIES: playlist
    //EFFECTS: open multiple audio files under specified directory, convert each AudioFiles to Song and Add to Playlist.
    //     skip files that are not of audio format. Throw NullPointerException if filesLocation not exists.
    public void saveMultipleAudioFilesToPlaylist(String filesLocation, Playlist playlist){
        File dir = new File(filesLocation);
        AudioParser ap = new AudioParser();


        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                try {
                    Song parsedSong = ap.parseFileToSong(file);
                    playlist.addSong(parsedSong);
                } catch (NotAudioFileException e) {
                    System.out.println("Detected a file that is not audio: "+file.getName());
                    System.out.println("Skipping to the next file... \n");
                } catch (AlreadyInPlaylistException e){
                    System.out.println(file.getName() + " is already in playlist.");
                    System.out.println("Skipping to the next file... \n");
                }
            }
            System.out.println("Here are the audio files saved:");
            playlist.print();

        } else {
            throw new NullPointerException();
        }
    }


    public void writeMainPlaylistFile(){
        try {
            mainPlaylist.writeToFile(gson.toJson(mainPlaylist));
        } catch (EmptyPlaylistException e){
            System.out.println("Error: Playlist is empty");
        }
    }


    public static void main(String[] args){
        Main m = new Main();
//        m.inputSongsToPlaylist();
        m.saveMultipleAudioFilesToPlaylist(audioFilesLocation, mainPlaylist);
        m.askEachSongStatus();
        m.writeMainPlaylistFile();

    }

}

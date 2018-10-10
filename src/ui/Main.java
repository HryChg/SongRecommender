package ui;

import com.google.gson.Gson;
import model.AudioParser;
import model.Playlist;
import model.Song;
import org.apache.tika.Tika;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Main {
    private Playlist playlist = new Playlist("MainPlaylist");
    private Scanner scanner = new Scanner(System.in);
    private Timestamp currentTimeStamp = new Timestamp(0);
    private ArrayList<File> openedFiles = new ArrayList<>();
    private Gson gson = new Gson();


    //GETTERS
    public ArrayList<File> getOpenedFiles() {
        return openedFiles;
    }


    // MODIFIES: playlist, scanner
    // EFFECTS: prompt user to input songs into a playlist
    public void inputSongsToPlaylist() {
        playlist.setPlayListName("uiPlaylist");

        // asking user to input songs
        boolean stillExecute = true;
        while (stillExecute) {
            System.out.println("Please input a song name: (enter q to quit)");
            String name = scanner.nextLine();

            if (!name.equals("q")) {
                Song curr_song = new Song("test");
                curr_song.setSongName(name);
                playlist.addSong(curr_song);
                System.out.println("The songs <" + name + "> has been added");
            } else {
                playlist.print();
                stillExecute = false;
            }


        }
    }


    // MODIFIES: playlist
    //EFFECTS: ask user if first time playing a song or recently played it.
    public void askEachSongStatus() {
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


    //EFFECTS: return the file type of a given file
    private String detectFile(File file) throws IOException {
        Tika tika = new Tika();
        String type = tika.detect(file);
        return type;
    }


    //MODIFIES: this
    //EFFETCS: open multiple audio files under specified directory, convert each to Song and Add to Playlist
    public void openMultipleFiles() throws IOException {
        String fileLocation = "songs";
        File dir = new File(fileLocation);


        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                String fileType = detectFile(file);
                if (file.isFile() && fileType.equals("audio/mpeg")) {
                    openedFiles.add(file);
                }
            }
            System.out.println(openedFiles);
        } else {
            System.out.println("This directory does not exist!");
        }
    }


    //EFFECTS: Create a Song for each element in the opened files
    public void convertFilesToSongs(List<File> openedFiles){
        AudioParser ap = new AudioParser();

        for (File file: openedFiles){
            Song currentSong = ap.parseFileToSong(file);
            playlist.addSong(currentSong);
        }
    }

    public void writeMainPlaylistFile() throws IOException{
        playlist.writeToFile(gson.toJson(playlist));
    }




    public static void main(String[] args) throws IOException {
        Main m = new Main();
//        m.inputSongsToPlaylist();
        m.openMultipleFiles();
        m.convertFilesToSongs(m.getOpenedFiles());
        m.askEachSongStatus();
        m.writeMainPlaylistFile();

    }

}

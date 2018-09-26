package ui;

import model.Playlist;
import model.Song;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;


public class Main {
    private Playlist playlist = new Playlist();
    private Scanner scanner = new Scanner(System.in);
    private Timestamp currentTimeStamp = new Timestamp(0);


    // MODIFIES: playlist scanner
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
                playlist.printPlayList();
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
            while(!userReply) {
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

        System.out.println("Number of Songs Updated: "+countSongUpdated);

    }


    public static void main(String[] args) {
        Main m = new Main();
        m.inputSongsToPlaylist();
        m.askEachSongStatus();


    }

}

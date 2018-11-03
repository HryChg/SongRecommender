package model;

import exceptions.AlreadyInPlaylistException;
import exceptions.NotAudioFileException;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

public class PlaylistManager {
    private AudioParser ap;
    private Timestamp currentTimeStamp;
    private Scanner scanner;


    public PlaylistManager(){

        currentTimeStamp = new Timestamp(0);
        scanner = new Scanner(System.in);
    }


    //MODIFIES: playlist
    //EFFECTS: open multiple audio files under specified directory, convert each AudioFiles to Song and Add to Playlist.
    //     skip files that are not of audio format or are already in playlist.
    //     Throw NullPointerException if filesLocation not exists.
    public void saveMultipleAudioFilesToPlaylist(String filesLocation, Playlist playlist) throws NullPointerException{
        File dir = new File(filesLocation);

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                try {
                    ap = new AudioParser(); //remember to always create a new parser for each new file!
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

        } else {
            throw new NullPointerException();
        }

        System.out.println("Audio Files transfer to playlist: "+playlist.getPlayListName()+" completed. \n");

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


}

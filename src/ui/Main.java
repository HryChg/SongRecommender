package ui;

import com.google.gson.Gson;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import exceptions.AlreadyInPlaylistException;
import exceptions.EmptyPlaylistException;
import exceptions.EmptyStringException;
import exceptions.NotAudioFileException;
import model.AudioParser;
import model.Playlist;
import model.PlaylistManager;
import model.Song;


import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;


public class Main {

    private static String audioFilesLocation = "songs";
    private static PlaylistManager pm = new PlaylistManager();
    private static Gson gson = new Gson();



    public static void main(String[] args){
        Playlist mainPlaylist = new Playlist("MainPlaylist");

        try {
            pm.saveMultipleAudioFilesToPlaylist(audioFilesLocation, mainPlaylist);
            mainPlaylist.print();
            pm.askEachSongStatus(mainPlaylist);
            mainPlaylist.writeToFile(gson.toJson(mainPlaylist));
        } catch (EmptyPlaylistException e){
            System.out.println("Not expected to fail");
        }
    }

}

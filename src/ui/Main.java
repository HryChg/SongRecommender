package ui;

import exceptions.EmptyPlaylistException;
import model.*;


public class Main {

    private static String audioFilesLocation = "songs";
    private static PlaylistManager pm = new PlaylistManager();


    public static void main(String[] args) {
        Playlist mainPlaylist = new Playlist("MainPlaylist");


//        pm.saveMultipleAudioFilesToPlaylist(audioFilesLocation, mainPlaylist);
//        mainPlaylist.print();
//        pm.askEachSongStatus(mainPlaylist);
//        mainPlaylist.writeToFile(mainPlaylist.convertToGsonString());
        String mainPlaylistPath = "/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/" +
                "GitHub Repo/projectw1_team997/savedFiles/savedPlaylists/MainPlaylist.txt";
        String songPath = "/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/songs";

        pm.updateDatabase(mainPlaylistPath, songPath);
        mainPlaylist.readFromFile(mainPlaylistPath);
        mainPlaylist.print();
        //pm.askEachSongStatus(mainPlaylist);



    }

}

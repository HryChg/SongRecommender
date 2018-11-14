package ui;

import Observer.Observer;
import com.google.gson.Gson;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import exceptions.AlreadyInPlaylistException;
import exceptions.EmptyPlaylistException;
import exceptions.EmptyStringException;
import exceptions.NotAudioFileException;
import model.*;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;


public class Main {

    private static String audioFilesLocation = "songs";
    private static PlaylistManager pm = new PlaylistManager();




    public static void main(String[] args){
//        Playlist mainPlaylist = new Playlist("MainPlaylist");
//
//        try {
//            pm.saveMultipleAudioFilesToPlaylist(audioFilesLocation, mainPlaylist);
//            mainPlaylist.print();
//            pm.askEachSongStatus(mainPlaylist);
//            mainPlaylist.writeToFile(mainPlaylist.convertToGsonString());
//        } catch (EmptyPlaylistException e){
//            System.out.println("Not expected to fail");
//        }



        //EXTRACT WEB DATA
        System.out.println("\n --------------- Now extracting web data!!! --------------- \n");
        System.out.println("Extracting...");
        try {
            String theURL = "https://www.ugrad.cs.ubc.ca/~cs210/2018w1/welcomemsg.html";

            WebParser webParser = new WebParser();
            Observer onelineReader1 = new OnlineReader();

            //adding observer to subject
            webParser.addObserver(onelineReader1);

            //perform webdata parsing
            StringBuilder sb = webParser.ExtractWebData(theURL);

            System.out.println("\nHere is the Web Data:");
            System.out.println(sb);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }


    }

}

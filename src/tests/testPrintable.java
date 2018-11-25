package tests;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import exceptions.AlreadyInPlaylistException;
import model.Playlist;
import model.Printable;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class testPrintable extends abstractTestPrint {
    Timestamp testTS = new Timestamp(new Date().getTime());

    private Song september = new Song("September");
    private Song lostInTheLight = new Song("LostInTheLight");
    private Song islands = new Song("Islands");


    @Test
    public void testPrintSong() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // After this all System.out.println() statements will come to outContent stream.

        // Printing the song
        Song testSong = new Song("testSong");
        testSong.setLastPlayedDate(testTS);
        testSong.setPlayedTime(testTS);

        SimpleDateFormat DateFormat = new SimpleDateFormat("E yyyy.MM.dd");
        SimpleDateFormat TimeFormat = new SimpleDateFormat("kk:mm:ss");

        //Now you have to validate the output.
        String expectedOutput = "Song Name: testSong\n" +
                "Favorite:  false\n" +
                "Hate:      false\n" +
                "Last Played Date: " + DateFormat.format(testTS) + "\n" +
                "Last Played Time: " + TimeFormat.format(testTS) + "\n";

        verifyPrint(testSong, expectedOutput);
    }

    @Test
    public void testPrintSongNullDate() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // After this all System.out.println() statements will come to outContent stream.

        // Printing the song
        Song testSong = new Song("testSong");


        //Now you have to validate the output.
        String expectedOutput = "Song Name: testSong\n" +
                "Favorite:  false\n" +
                "Hate:      false\n" +
                "Warning: LastPlayedDate or PlayedTime is null.\n";


        verifyPrint(testSong, expectedOutput);
    }

    @Test
    public void printPlayListEmpty() {
        Playlist p = new Playlist("testPlaylist");
        String expectedOutput = "Current Playlist: testPlaylist\n";
        verifyPrint(p, expectedOutput);
    }

    @Test
    public void printPlayListOneSong() {

        Playlist p = new Playlist("testPlaylist");
        p.addSong(september);

        String expectedOutput = "Current Playlist: testPlaylist\n- September\n";
        verifyPrint(p, expectedOutput);
    }


    @Test
    public void printPlayListThreeSong() {
        Playlist p = new Playlist("testPlaylist");

        p.addSong(september);
        p.addSong(lostInTheLight);
        p.addSong(islands);

        String expectedOutput = "Current Playlist: testPlaylist\n- September\n- LostInTheLight\n- Islands\n";
        verifyPrint(p, expectedOutput);
    }

}

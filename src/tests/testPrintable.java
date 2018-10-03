package tests;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
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

public class testPrintable {
    Timestamp testTS = new Timestamp(new Date().getTime());
    List<String> testList = Arrays.asList("testSong", "false", "false", testTS.toString(), testTS.toString());




    @Test
    public void testPrint(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // After this all System.out.println() statements will come to outContent stream.

        // Printing the song
        Song song = new Song("testSong");
        song.setLastPlayedDate(testTS);
        song.setPlayedTime(testTS);

        SimpleDateFormat DateFormat = new SimpleDateFormat("E yyyy.MM.dd");
        SimpleDateFormat TimeFormat = new SimpleDateFormat("kk:mm:ss");

        Printable testSong = song;
        testSong.print();

        //Now you have to validate the output.
        String expectedOutput  = "Song Name: testSong\n" +
                "Favorite:  false\n"+
                "Hate:      false\n"+
                "Last Played Date: "+DateFormat.format(testTS)+"\n"+
                "Last Played Time: "+TimeFormat.format(testTS)+"\n";

        assertEquals(expectedOutput, outContent.toString());

    }

}

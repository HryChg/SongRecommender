package tests;

import com.google.gson.Gson;
import exceptions.EmptyStringException;
import model.Playlist;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class testSong {
    private Song testSong;
    private Timestamp testTimeStampForLastPlayedDate = new Timestamp(0);
    private Timestamp testTimeStampForPlayedTime = new Timestamp(0);
    private Timestamp testTimeStampForWriteToFile = new Timestamp(new Date().getTime());
    private Gson gson = new Gson();


    @BeforeEach
    public void setup() {
        testSong = new Song("September");
    }

    @Test
    public void testSetUp() {
        assertEquals(testSong.getSongName(), "September");
        assertFalse(testSong.getIsFavorite());
        assertFalse(testSong.getIsHate());
        assertEquals(testSong.getLastPlayedDate(), null);
        assertEquals(testSong.getPlayedTime(), null);
    }

    @Test
    public void testSetUpEmptyString() {
        try {
            testSong.setSongName("");
            fail("Not supposed to reach this line.");
        } catch (EmptyStringException e) {

        }
    }

    @Test
    public void testSetIsFavorite() {
        assertFalse(testSong.getIsFavorite());
        testSong.setIsFavorite(true);
        assertTrue(testSong.getIsFavorite());
        testSong.setIsFavorite(false);
        assertFalse(testSong.getIsFavorite());
    }

    @Test
    public void testSetIsHate() {
        assertFalse(testSong.getIsHate());
        testSong.setIsHate(true);
        assertTrue(testSong.getIsHate());
        testSong.setIsHate(false);
        assertFalse(testSong.getIsHate());
    }

    @Test
    public void testSetLastPlayed() {
        // make sure that testSong.getLastPlayedDate has not been assigned a value
        assertNull(testSong.getLastPlayedDate());

        // assigned testTimeStamp with Today's date and time and pass it to testSong
        testTimeStampForLastPlayedDate.setTime(new Date().getTime());
        testSong.setLastPlayedDate(testTimeStampForLastPlayedDate);
        assertEquals(testSong.getLastPlayedDate(), testTimeStampForLastPlayedDate);
    }

    @Test
    public void testSetTiming() {
        assertNull(testSong.getPlayedTime());
        testTimeStampForPlayedTime.setTime(new Date().getTime());
        testSong.setLastPlayedDate(testTimeStampForPlayedTime);
    }

    @Test
    public void testPrintLastPlayed() {
        assertNull(testSong.getLastPlayedDate());
        testTimeStampForLastPlayedDate.setTime(new Date().getTime());
        testSong.setLastPlayedDate(testTimeStampForLastPlayedDate);
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd");
        assertEquals(testSong.printLastPlayedDate(), ft.format(testTimeStampForLastPlayedDate));
    }


    @Test
    public void testPrintTiming() {
        assertNull(testSong.getPlayedTime());
        testTimeStampForPlayedTime.setTime(new Date().getTime());
        testSong.setPlayedTime(testTimeStampForPlayedTime);

        SimpleDateFormat ft = new SimpleDateFormat("kk:mm:ss");
        assertEquals(ft.format(testTimeStampForPlayedTime), testSong.printPlayedTime());
    }


    @Test
    public void testWriteToFile() throws Exception {
        testSong.setIsFavorite(true);
        testSong.setLastPlayedDate(testTimeStampForWriteToFile);
        testSong.setPlayedTime(testTimeStampForWriteToFile);
        String myData = testSong.convertToGsonString();
        testSong.writeToFile(myData);

        //Convert the text file to lines
        Path filePath = Paths.get("savedFiles/savedSongs/September.txt");
        List<String> lines = Files.readAllLines(filePath);
        String firstLine = lines.get(0);

        //Format the date
        SimpleDateFormat ft = new SimpleDateFormat("MMM d, yyyy h:mm:ss a");

        //Expected outcome
        String expectedOutput = "{\"songName\":\"September\"," +
                "\"isFavorite\":true," +
                "\"isHate\":false," +
                "\"lastPlayedDate\":\"" + ft.format(testTimeStampForWriteToFile) + "\"," +
                "\"playedTime\":\"" + ft.format(testTimeStampForWriteToFile) + "\"}";

        //assertEquals
        assertEquals(expectedOutput, firstLine);
    }

    @Test
    public void testReadFromFile() {
        Timestamp ts = new Timestamp(new Date().getTime());
        Song song = new Song("testReadSongFile");

        String filePath = "/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/src/tests/testFiles/testReadSongFile.txt";
        song.readFromFile(filePath); //loading stored info back to song

        assertEquals("testReadSongFile", song.getSongName());
        assertTrue(song.getIsFavorite());
        assertTrue(song.getIsHate());
        assertEquals("2018-10-02 23:55:40.0", song.getLastPlayedDate().toString());
        assertEquals("2018-10-02 23:55:40.0", song.getPlayedTime().toString());

    }


    @Test
    public void testEqualsSameReference(){
        Song testSongSameReference = testSong;
        assertTrue(testSong.equals(testSongSameReference));
    }

    @Test
    public void testEqualsNull(){
        Song nullSong = null;
        assertFalse(testSong.equals(nullSong));
    }

    @Test
    public void testEequalsTwoSongs(){
        Song testSongDifferentObject = new Song("September");
        assertTrue(testSong.equals(testSongDifferentObject));
    }

    //YOU DONT TEST HASHCODE
//    @Test
//    public void testHashCode(){
//        Song testSongDifferentObject = new Song("September");
//        assertEquals(testSong.hashCode(), testSongDifferentObject.hashCode());
//
//
//    }


}

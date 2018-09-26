package tests;

import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class testSong {
    private Song testSong;
    private Timestamp testTimeStampForLastPlayedDate = new Timestamp(0);
    private Timestamp testTimeStampForPlayedTime = new Timestamp(0);


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
    public void testSetIsFavorite(){
        assertFalse(testSong.getIsFavorite());
        testSong.setIsFavorite(true);
        assertTrue(testSong.getIsFavorite());
        testSong.setIsFavorite(false);
        assertFalse(testSong.getIsFavorite());
    }

    @Test
    public void testSetIsHate(){
        assertFalse(testSong.getIsHate());
        testSong.setIsHate(true);
        assertTrue(testSong.getIsHate());
        testSong.setIsHate(false);
        assertFalse(testSong.getIsHate());
    }

    @Test
    public void testSetLastPlayed(){
        // make sure that testSong.getLastPlayedDate has not been assigned a value
        assertNull(testSong.getLastPlayedDate());

        // assigned testTimeStamp with Today's date and time and pass it to testSong
        testTimeStampForLastPlayedDate.setTime(new Date().getTime());
        testSong.setLastPlayedDate(testTimeStampForLastPlayedDate);
        assertEquals(testSong.getLastPlayedDate(), testTimeStampForLastPlayedDate);
    }

    @Test
    public void testSetTiming(){
        assertNull(testSong.getPlayedTime());
        testTimeStampForPlayedTime.setTime(new Date().getTime());
        testSong.setLastPlayedDate(testTimeStampForPlayedTime);
    }

    @Test
    public void testPrintLastPlayed(){
        assertNull(testSong.getLastPlayedDate());
        testTimeStampForLastPlayedDate.setTime(new Date().getTime());
        testSong.setLastPlayedDate(testTimeStampForLastPlayedDate);
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd");
        assertEquals(testSong.printLastPlayedDate(), ft.format(testTimeStampForLastPlayedDate));
    }


    @Test
    public void testPrintTiming(){
        assertNull(testSong.getPlayedTime());
        testTimeStampForPlayedTime.setTime(new Date().getTime());
        testSong.setPlayedTime(testTimeStampForPlayedTime);

        SimpleDateFormat ft = new SimpleDateFormat ("kk:mm:ss");
        assertEquals(ft.format(testTimeStampForPlayedTime), testSong.printPlayedTime());
    }




}

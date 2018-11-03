package tests;

import com.google.gson.Gson;
import exceptions.AlreadyInPlaylistException;
import exceptions.EmptyPlaylistException;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class testPlaylist extends abstractTestPrint {
    private Playlist p;
    private Song nullSong = null;
    private Song september = new Song("September");
    private Song lostInTheLight = new Song("LostInTheLight");
    private Song islands = new Song("Islands");
    private Song laLaLand = new Song("LaLaLand");

    private Gson gson = new Gson();


    @BeforeEach
    public void setup() {
        p = new Playlist("testPlaylist");

    }


    @Test
    public void testPlaylist() {
        Playlist testP = new Playlist("testPlaylist");
        assertTrue(testP.getListOfSongs().isEmpty());
        assertTrue(testP.getSongAddDates().isEmpty());
    }

    @Test
    public void testSetPlaylistName() {
        try {
            assertEquals(p.getPlayListName(), "testPlaylist");
            p.setPlaylistName("testPlaylistName");
            assertEquals(p.getPlayListName(), "testPlaylistName");
        } catch (EmptyStringException e) {
            fail("Fail: EmptyStringException is not supposed to happen");
        }
    }

    @Test
    public void testSetPLaylistNameEmptyStringException() {
        try {
            assertEquals(p.getPlayListName(), "testPlaylist");
            p.setPlaylistName("");
            fail("Fail: EmptyStringException is not supposed to happen");
        } catch (EmptyStringException e) {

        } finally {
            assertEquals(p.getPlayListName(), "testPlaylist");
        }
    }

    @Test
    public void testGetSongThreeSongPlaylist() {

        try {
            p.addSong(september);
            p.addSong(lostInTheLight);
            p.addSong(islands);
        } catch (AlreadyInPlaylistException e) {
            fail("Fail: AlreadyInPlaylistException is not supposed to happen");
        }

        assertEquals(p.getSize(), 3);

        assertEquals(september, p.getSong(0));
        assertEquals(lostInTheLight, p.getSong(1));
        assertEquals(islands, p.getSong(2));
    }

    @Test
    public void testAddSongNull() {
        try {
            p.addSong(nullSong);
            fail("");
        } catch (AlreadyInPlaylistException e) {
            fail("");
        } catch (NullPointerException e) {

        }
        assertEquals(p.getSize(), 0);

    }

    @Test
    public void testAddSongOne() {

        //!!! this is not correct there is discrepancy between the testTimeStamp and time of adding song
        Timestamp testTimestamp = new Timestamp(new Date().getTime());

        try {
            p.addSong(september);

        } catch (AlreadyInPlaylistException e) {
            fail("AlreadyInPlaylistException is not supposed to happen");
        } catch (NullPointerException e) {
            fail("NullPointerException is not supposed to happen");
        }

        assertEquals(p.getSize(), 1);
        assertTrue(p.contains(september));
        assertEquals(p.getSongAddDates().get(september), testTimestamp);
    }

    @Test
    public void testAddSongThree() {
        try {//!!! not sure how to test time here since they are added so closely
            p.addSong(september);
            assertEquals(p.getSize(), 1);
            assertTrue(p.contains(september));
            p.addSong(lostInTheLight);
            assertEquals(p.getSize(), 2);
            assertTrue(p.contains(lostInTheLight));
            p.addSong(islands);
            assertEquals(p.getSize(), 3);
            assertTrue(p.contains(islands));
        } catch (AlreadyInPlaylistException e) {
            fail("Fail: AlreadyInPlaylistException is not supposed to happen");
        } catch (NullPointerException e) {
            fail("Fail: NullPointerException is not supposed to happen");
        }

    }

    @Test
    public void testAddRepeatSong() {
        try {
            p.addSong(september);
            p.addSong(lostInTheLight);
            p.addSong(islands);
            assertEquals(p.getSize(), 3);
        } catch (AlreadyInPlaylistException e) {
            fail("");
        } catch (NullPointerException e) {
            fail("");
        }

        try {
            p.addSong(islands);
            fail("Fail: Supposed to catch AlreadyInPlaylistException");
        } catch (AlreadyInPlaylistException e) {

        } catch (NullPointerException e) {
            fail("Fail: NullPointerException is not supposed to happend");
        }

        assertEquals(p.getSize(), 3);
        assertTrue(p.contains(islands));
    }

    @Test
    public void testWriteToFile(){
        try {
            p.setPlaylistName("testPlaylistWriteToFile");
            Timestamp testTimestamp = new Timestamp(new Date().getTime());
            p.addSong(september);
            p.addSong(lostInTheLight);
            p.addSong(islands);

            p.writeToFile(p.convertToGsonString());

            //formatting the date
            SimpleDateFormat ft = new SimpleDateFormat("MMM d, yyyy h:mm:ss a");
            ft.format(testTimestamp);

            String expectedOutput = "{\"playListName\":\"testPlaylistWriteToFile\",\"listOfSongs\":" +
                    "[{\"songName\":\"September\",\"isFavorite\":false,\"isHate\":false}," +
                    "{\"songName\":\"LostInTheLight\",\"isFavorite\":false,\"isHate\":false}," +
                    "{\"songName\":\"Islands\",\"isFavorite\":false,\"isHate\":false}]," +
                    "\"songAddDates\":{" +
                    "\"model.Song@fe7514b1\":\"" + ft.format(testTimestamp) +"\"," +
                    "\"model.Song@79371ce\":\""  + ft.format(testTimestamp) +"\"," +
                    "\"model.Song@e003e0de\":\"" + ft.format(testTimestamp) +"\"}}";

            Path filePath = Paths.get("savedFiles/savedPlaylists/testPlaylistWriteToFile.txt");
            List<String> lines = Files.readAllLines(filePath);
            String firstLine = lines.get(0);

            assertEquals(expectedOutput, firstLine);

        } catch (EmptyStringException e) {
            System.out.println("Oop. setPlaylistName detects an emptyString for playlistName");
        } catch (AlreadyInPlaylistException e) {
            fail("");
        } catch (NullPointerException e) {
            fail("");
        } catch (EmptyPlaylistException e) {
            fail("Playlist is empty!");
        } catch(IOException e){
            fail(e.toString());
        }
    }

    @Test
    public void testWriteToFileEmptyPlaylistException(){
        try {
            p.setPlaylistName("testPlaylistWriteToFile");
            p.writeToFile(p.convertToGsonString());
            fail("empty playlist should not have been able to be saved to a file");
        } catch (EmptyStringException e) {
            System.out.println("Oop. setPlaylistName detects an emptyString for playlistName");
        } catch (EmptyPlaylistException e) {

        }
    }

    @Test
    public void testReadFromFile() {
        p.readFromFile("savedFiles/savedPlaylists/testPlaylistReadFromFile.txt");

        assertEquals("testPlaylistReadFromFile", p.getPlayListName());

        List<Song> expectedListOfSongs = Arrays.asList(september, lostInTheLight, islands);
        List<Song> actualListOfSongs = p.getListOfSongs();
        assertEquals(expectedListOfSongs, actualListOfSongs);
    }

    @Test
    public void testEquals(){
        //test if two var with the same object references
        Playlist p2 = p;
        assertTrue(p.equals(p2));

        //test if two empty playlists with same name are equals
        Playlist p3 = new Playlist("testPlaylist");
        assertTrue(p.equals(p3));


        try {
            //test if two playlist with same order of songs are equal
            p.addSong(september);
            p3.addSong(september);
            assertTrue(p.equals(p3));

            p.addSong(islands);
            p3.addSong(islands);
            assertTrue(p.equals(p3));

            //test if two playlist with different order of songs are equal
            p.addSong(lostInTheLight);
            p3.addSong(laLaLand);
            p.addSong(laLaLand);
            p3.addSong(lostInTheLight);
            assertFalse(p.equals(p3));

        } catch (AlreadyInPlaylistException e) {
            fail("");
        }
    }

    @Test
    public void testHashCode(){
        //test if two empty playlists with same name generate same hashCode
        Playlist p2 = new Playlist("testPlaylist");
        assertEquals(p2.hashCode(), p.hashCode());

        try {
            //test if adding the same song will lead to same hashCode
            p.addSong(september);
            p2.addSong(september);
            assertEquals(p2.hashCode(), p.hashCode());

            p.addSong(lostInTheLight);
            p2.addSong(lostInTheLight);
            assertEquals(p2.hashCode(), p.hashCode());

            //test if two playlists containing different songs generate same hashCode
            p.addSong(islands);
            assertNotEquals(p2.hashCode(), p.hashCode());

            //test two playlist with different order of the same songs generate same hashCode
            p2.addSong(laLaLand);
            p2.addSong(islands);                       //p2: september, lostInTheLight, laLaLand, islands
            p.addSong(laLaLand);     //p: september, lostInTheLight, islands, laLaLand
            assertNotEquals(p2.hashCode(), p.hashCode());

        }catch (AlreadyInPlaylistException e){
            fail("");
        }
    }

}

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class testPlaylist extends abstractTestPrint {
    private Playlist p;
    private Song nullsong = null;
    private Song september = new Song("September");
    private Song lostInTheLight = new Song("LostInTheLight");
    private Song islands = new Song("Islands");
    private Gson gson = new Gson();


    @BeforeEach
    public void setup() {
        p = new Playlist("testPlaylist");

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
    public void testSetPLaylistNameEmptyString() {
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
    public void testGetSongEmptyPlaylist() {
        assertEquals(null, p.getSong(0));

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
            p.addSong(nullsong);
            fail("");
        } catch (AlreadyInPlaylistException e) {
            fail("");
        } catch (NullPointerException e) {

        }
        assertEquals(p.getSize(), 0);

    }

    @Test
    public void testAddSongOne() {
        try {
            p.addSong(september);
        } catch (AlreadyInPlaylistException e) {
            fail("AlreadyInPlaylistException is not supposed to happen");
        } catch (NullPointerException e) {
            fail("NullPointerException is not supposed to happen");
        }

        assertEquals(p.getSize(), 1);
        assertTrue(p.contains(september));

    }

    @Test
    public void testAddSongThree() {
        try {
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

            p.addSong(september);
            p.addSong(lostInTheLight);
            p.addSong(islands);

            p.writeToFile(gson.toJson(p));


            String expectedOutput = "{\"playListName\":\"testPlaylistWriteToFile\",\"listOfSongs\":" +
                    "[{\"songName\":\"September\",\"isFavorite\":false,\"isHate\":false}," +
                    "{\"songName\":\"LostInTheLight\",\"isFavorite\":false,\"isHate\":false}," +
                    "{\"songName\":\"Islands\",\"isFavorite\":false,\"isHate\":false}]}";

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
    public void testWriteToFileEmptyPlaylistEx(){
        try {
            p.setPlaylistName("testPlaylistWriteToFile");
            p.writeToFile(gson.toJson(p));
            fail("empty playlist should not have been able to be saved to a file");
        } catch (EmptyStringException e) {
            System.out.println("Oop. setPlaylistName detects an emptyString for playlistName");
        } catch (EmptyPlaylistException e) {

        }
    }


    @Test
    public void testReadFromFile() {
        List<String> expectedListOfSongs = Arrays.asList(september.getSongName(),
                lostInTheLight.getSongName(), islands.getSongName());
        List<String> actualListOfSongs = new ArrayList<>();

        p.readFromFile("savedFiles/savedPlaylists/testPlaylistReadFromFile.txt");

        assertEquals("testPlaylistReadFromFile", p.getPlayListName());

        for (Song song : p.getListOfSongs()) {
            actualListOfSongs.add(song.getSongName());
        }
        //have to verify just the song name because they are different objects and you cannot ask java to test if they
        //    contain the same value
        assertEquals(expectedListOfSongs, actualListOfSongs);

    }


}

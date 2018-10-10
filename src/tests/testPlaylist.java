package tests;

import com.google.gson.Gson;
import exceptions.nullException;
import model.Playlist;
import model.Printable;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
    public void testSetUp() {
        assertEquals(p.getPlayListName(), "testPlaylist");
        p.setPlayListName("testPlaylistName");
        assertEquals(p.getPlayListName(), "testPlaylistName");
        assertEquals(p.getSize(), 0);
    }

    @Test
    public void testGetSongEmptyPlaylist() {
        assertEquals(null, p.getSong(0));

    }

    @Test
    public void testGetSongThreeSongPlaylist() {
        p.addSong(september);
        p.addSong(lostInTheLight);
        p.addSong(islands);
        assertEquals(p.getSize(), 3);

        assertEquals(september, p.getSong(0));
        assertEquals(lostInTheLight, p.getSong(1));
        assertEquals(islands, p.getSong(2));
    }

    @Test
    public void testAddSongNull() {
        assertEquals(p.getSize(), 0);
        p.addSong(nullsong);
        assertEquals(p.getSize(), 0);
        assertFalse(p.contains(september));
    }

    @Test
    public void testAddSongOne() {
        assertEquals(p.getSize(), 0);
        p.addSong(september);
        assertEquals(p.getSize(), 1);
        assertTrue(p.contains(september));

    }

    @Test
    public void testAddSongThree() {
        assertEquals(p.getSize(), 0);
        p.addSong(september);
        assertEquals(p.getSize(), 1);
        assertTrue(p.contains(september));
        p.addSong(lostInTheLight);
        assertEquals(p.getSize(), 2);
        assertTrue(p.contains(lostInTheLight));
        p.addSong(islands);
        assertEquals(p.getSize(), 3);
        assertTrue(p.contains(islands));
    }

    @Test
    public void testAddRepeatSong(){
        p.addSong(september);
        p.addSong(lostInTheLight);
        p.addSong(islands);
        assertEquals(p.getSize(), 3);

        p.addSong(islands);
        assertEquals(p.getSize(), 3);
        assertTrue(p.contains(islands));





    }


    @Test
    public void testWriteToFile() throws IOException {
        p.setPlayListName("testPlaylistWriteToFile");
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

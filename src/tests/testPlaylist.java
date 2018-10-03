package tests;

import com.google.gson.Gson;
import model.Playlist;
import model.Song;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class testPlaylist {
    private Playlist p;
    private Song nullsong = null;
    private Song september = new Song("September");
    private Song lostInTheLight= new Song ("LostInTheLight");
    private Song islands = new Song("Islands");
    private Gson gson = new Gson();



    @BeforeEach
    public void setup(){
        p = new Playlist("testPlaylist");

    }

    @Test
    public void testSetUp(){
        assertEquals(p.getPlayListName(), "testPlaylist");
        p.setPlayListName("testPlaylistName");
        assertEquals(p.getPlayListName(),"testPlaylistName");
        assertEquals(p.getSize(), 0);
    }

    @Test
    public void testGetSongEmptyPlaylist(){
        assertEquals(null, p.getSong(0));

    }

    @Test
    public void testGetSongThreeSongPlaylist(){
        p.addSong(september);
        p.addSong(lostInTheLight);
        p.addSong(islands);
        assertEquals(p.getSize(), 3);

        assertEquals(september, p.getSong(0));
        assertEquals(lostInTheLight, p.getSong(1));
        assertEquals(islands, p.getSong(2));
    }

    @Test
    public void testAddSongNull(){
        assertEquals(p.getSize(), 0);
        p.addSong(nullsong);
        assertEquals(p.getSize(), 0);
        assertFalse(p.contains(september));

    }

    @Test
    public void testAddSongOne(){
        assertEquals(p.getSize(), 0);
        p.addSong(september);
        assertEquals(p.getSize(), 1);
        assertTrue(p.contains(september));

    }

    @Test
    public void testAddSongThree(){
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
    public void printPlayListEmpty(){
        assertEquals(p.printPlayList().size(), 0);
    }

    @Test
    public void printPlayListOneSong(){
        p.addSong(september);
        assertEquals(p.printPlayList().size(), 1);
    }


    @Test
    public void printPlayListThreeSong(){
        p.addSong(september);
        p.addSong(lostInTheLight);
        p.addSong(islands);

        assertEquals(p.printPlayList().size(), 3);
    }


    @Test
    public void testWriteToFile() throws IOException{
        p.setPlayListName("FavoritePlaylist");
        p.addSong(september);
        p.addSong(lostInTheLight);
        p.addSong(islands);
        p.writeToFile(gson.toJson(p));

        String expectedOutput = "{\"playListName\":\"FavoritePlaylist\",\"listOfSongs\":[{\"songName\":\"September\",\"isFavorite\":false,\"isHate\":false},{\"songName\":\"LostInTheLight\",\"isFavorite\":false,\"isHate\":false},{\"songName\":\"Islands\",\"isFavorite\":false,\"isHate\":false}]}";

        Path filePath = Paths.get("/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/src/savedFiles/savedPlaylists/FavoritePlaylist.txt");
        List<String> lines = Files.readAllLines(filePath);
        String firstLine = lines.get(0);

        assertEquals(expectedOutput, firstLine);

    }

    @Test
    public void testReadFromFile(){
        List<String> expectedListOfSongs = Arrays.asList(september.getSongName(), lostInTheLight.getSongName(), islands.getSongName());
        List<String> actualListOfSongs = new ArrayList<>();

        p.readFromFile("/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/src/savedFiles/savedPlaylists/FavoritePlaylist.txt");

        assertEquals("FavoritePlaylist", p.getPlayListName());

        for (Song song:p.getListOfSongs()){
            actualListOfSongs.add(song.getSongName());
        }
        assertEquals(expectedListOfSongs, actualListOfSongs);


    }




}

package tests;

import model.Playlist;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class testPlaylist {
    private Playlist p;
    private Song nullsong = null;
    private Song september = new Song("September");
    private Song lostInTheLight= new Song ("LostInTheLight");
    private Song islands = new Song("Islands");



    @BeforeEach
    public void setup(){
        p = new Playlist();
        p.setPlayListName("testPlaylist");
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

    }

    @Test
    public void testAddSongOne(){
        assertEquals(p.getSize(), 0);
        p.addSong(september);
        assertEquals(p.getSize(), 1);

    }

    @Test
    public void testAddSongThree(){
        assertEquals(p.getSize(), 0);
        p.addSong(september);
        assertEquals(p.getSize(), 1);
        p.addSong(lostInTheLight);
        assertEquals(p.getSize(), 2);
        p.addSong(islands);
        assertEquals(p.getSize(), 3);
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

}

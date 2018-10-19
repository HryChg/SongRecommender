package tests;

import model.AudioParser;
import model.Playlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Main;

import java.io.File;

import static org.jsoup.helper.Validate.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testMain {
    private Main m;



    @BeforeEach
    public void setup(){
        m = new Main();
    }



    @Test
    public void testSaveMultipleAudioFilesToPlaylistPathNotExist(){

        try{
            String filesLocation = "nonExistingPath";
            Playlist testPlaylist = new Playlist("testPlaylist");
            m.saveMultipleAudioFilesToPlaylist(filesLocation, testPlaylist);
            fail("");
        } catch (NullPointerException e){
            System.out.println("Success: caught NullPointerException");
        }
    }

    @Test
    public void testSaveMultipleAudioFilesToPlaylistPathExist(){
        try{
            String filesLocation = "songs";
            Playlist testPlaylist = new Playlist("testPlaylist");
            m.saveMultipleAudioFilesToPlaylist(filesLocation, testPlaylist);
            assertEquals(testPlaylist.getSize(),7);
        } catch (NullPointerException e){
            fail("");
        }
    }


}

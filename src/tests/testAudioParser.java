package tests;

import exceptions.NotAudioFileException;
import model.AudioParser;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.jsoup.helper.Validate.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testAudioParser {
    AudioParser ap;
    Song testSong = new Song("Best Friend");

    @BeforeEach
    public void setup(){
        ap = new AudioParser();
    }

    @Test
    public void parseFileToSongAudioFile(){
        try {
            File songFile = new File("src/tests/testFiles/Best Friend.mp3");
            Song song = ap.parseFileToSong(songFile);
            assertEquals(testSong.getSongName(), song.getSongName());
        } catch (NotAudioFileException e){
            fail("This exception is not supposed to happen!");
        }
    }

    @Test void parseFileToSongNotAudioFileException(){
        File songFile = new File("src/tests/testFiles/NotAudioFileExceptionTesting.jpeg");

        try {
            ap.parseFileToSong(songFile);
            fail("Did not catch NotAudioFileException");
        } catch (NotAudioFileException e){
            System.out.println("Successfully caught NotAudioFileException!");
        }

    }

    @Test void parseFileToSongNoFileExist(){
        File songFile = new File("src/tests/testFiles/NoSuchFilePath.mp3");
        try {
            Song currentSong = ap.parseFileToSong(songFile);
            assertEquals(currentSong, null);
        } catch (NotAudioFileException e){
            fail("");
        }
    }


}

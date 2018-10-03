package tests;

import model.AudioParser;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testAudioParser {
    AudioParser ap;
    Song testSong = new Song("Best Friend");

    @BeforeEach
    public void setup(){
        ap = new AudioParser();
    }

    @Test
    public void testAudioParser(){
        File songFile = new File("src/tests/testFiles/Best Friend.mp3");
        Song song = ap.parseFileToSong(songFile);
        assertEquals(testSong.getSongName(), song.getSongName());
    }

}

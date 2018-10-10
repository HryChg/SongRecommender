package tests;

import model.Playlist;
import model.Song;
import model.Queueable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class testQueueable {

    @Test
    public void testInsertOneSong(){


        Queueable q = new Song("testSong");
        q.insertQueue();

        Path filePath = Paths.get("savedFiles/savedQueue.txt");

        try {
            List<String> lines = Files.readAllLines(filePath);
            //check only the last entry in savedQueue
            assertEquals("- testSong", lines.get(lines.size()-1));
        } catch (IOException e){
            System.out.println("Error: " + e.toString());
        }


    }

    @Test
    public void testInsertOnePlaylist() throws IOException {
        Playlist p = new Playlist("testPlaylist");
        p.addSong(new Song("song1"));
        p.addSong(new Song("song2"));
        p.addSong(new Song("song3"));

        Queueable q = p;
        q.insertQueue();

        Path filePath = Paths.get("savedFiles/savedQueue.txt");
        List<String> lines = Files.readAllLines(filePath);
        int linesSize = lines.size();

        assertEquals("- song1", lines.get(linesSize-3));
        assertEquals("- song2", lines.get(linesSize-2));
        assertEquals("- song3", lines.get(linesSize-1));
    }

}

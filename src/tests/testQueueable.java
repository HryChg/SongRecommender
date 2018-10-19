package tests;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import exceptions.AlreadyInPlaylistException;
import model.Playlist;
import model.Song;
import model.Queueable;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class testQueueable {

    @Test
    public void testInsertOneSong() {
        Queueable q = new Song("testSong");
        q.insertQueue();

        Path filePath = Paths.get("savedFiles/savedQueue.txt");

        try {
            List<String> lines = Files.readAllLines(filePath);
            //check only the last entry in savedQueue
            assertEquals("- testSong", lines.get(lines.size() - 1));
        } catch (FileNotFoundException e) {
            fail("File not Found!");
        } catch (IOException e) {
            fail("Error: " + e.toString());
        }
    }

    @Test
    public void testInsertOnePlaylist(){
        Playlist p = new Playlist("testPlaylist");
        try {
            p.addSong(new Song("song1"));
            p.addSong(new Song("song2"));
            p.addSong(new Song("song3"));

            Queueable q = p;
            q.insertQueue();

            Path filePath = Paths.get("savedFiles/savedQueue.txt");
            List<String> lines = Files.readAllLines(filePath);
            int linesSize = lines.size();

            assertEquals("- song1", lines.get(linesSize - 3));
            assertEquals("- song2", lines.get(linesSize - 2));
            assertEquals("- song3", lines.get(linesSize - 1));


        } catch (AlreadyInPlaylistException e) {
            System.out.println("Error, song is already in the playlist");
        } catch (FileNotFoundException e){
            fail("Error, FILE NOT FOUND!");
        } catch (IOException e){
            System.out.println(e.toString());
        }


    }

}

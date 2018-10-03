package tests;

import model.Playlist;
import model.Song;
import model.Queueable;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class testQueueable {

    @Test
    public void testInsertOneSong() throws IOException {

        Queueable q = new Song("testSong");
        q.insert();

        Path filePath = Paths.get("/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/src/savedFiles", "savedQueue.txt");
        List<String> lines = Files.readAllLines(filePath);

        assertEquals("-testSong", lines.get(lines.size()-1));
    }

    @Test
    public void testInsertOnePlaylist() throws IOException {
        Playlist p = new Playlist("testPlaylist");
        p.addSong(new Song("song1"));
        p.addSong(new Song("song2"));
        p.addSong(new Song("song3"));

        Queueable q = p;
        q.insert();

        Path filePath = Paths.get("/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/src/savedFiles", "savedQueue.txt");
        List<String> lines = Files.readAllLines(filePath);
        int linesSize = lines.size();

        assertEquals("-song1", lines.get(linesSize-3));
        assertEquals("-song2", lines.get(linesSize-2));
        assertEquals("-song3", lines.get(linesSize-1));
    }

}

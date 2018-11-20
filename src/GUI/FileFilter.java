package GUI;

import javafx.scene.media.Media;
import model.Playlist;
import model.Song;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class FileFilter {
    private final String path = "songs";
    private ArrayList<File> filesFound;



    public FileFilter(){
        filesFound = new ArrayList<>();
    }


    public ArrayList<Media> allocatePlaylistToMP3(Playlist playlist){
        for (Song s : playlist.getListOfSongs()){
            File f = filter(s.getSongName());

        }

        return null;
    }


    private File filter(String name){
        name = name+".mp3";
        File f = new File(path+"/"+name);

        return f;
    }


    public static void main(String[] args) {
        FileFilter ff = new FileFilter();
        System.out.println(ff.filter("Azrael"));
    }

}

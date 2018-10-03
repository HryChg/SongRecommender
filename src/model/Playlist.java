package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;

import static org.apache.pdfbox.util.ErrorLogger.log;

public class Playlist implements Queueable{
    private String playListName;
    private ArrayList<Song> listOfSongs = new ArrayList<Song>();
    private static String fileLocation;
    private static Gson gson = new Gson(); //use to reading and writing to savedPlayList

    public Playlist(String name) {
        this.playListName = name;
    }

    // MODIFIES: this
    // EFFECTS: Setting the Playlist Name
    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    //GETTERS
    public String getPlayListName() {
        return this.playListName;
    }
    public ArrayList<Song> getListOfSongs() {
        return this.listOfSongs;
    }
    public int getSize() {
        return this.listOfSongs.size();
    }

    //EFFECTS: getting song in the playlist at specified index. Return null if the index is not in the range of playlist
    public Song getSong(int location) {
        if ((0 <= location) && (location <= (this.getSize() - 1))) {
            return this.getListOfSongs().get(location);
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: add Song to the playlist, do nothing if the song is null
    public void addSong(Song song) {
        if (song != null) {
            this.listOfSongs.add(song);
        }
    }

    //EFFECTS: return true if the song in contained in playlist
    public boolean contains(Song song) {
        if (this.listOfSongs.contains(song)) {
            return true;
        } else {
            return false;
        }
    }

    //EFFECTS: print out the names of all songs in the playlist
    public ArrayList<String> printPlayList() {
        ArrayList<String> songName = new ArrayList<String>();
        System.out.println("Current Playlist: " + this.getPlayListName());
        for (Song s : listOfSongs) {
            System.out.println("- " + s.getSongName());
            songName.add(s.getSongName());
        }
        return songName;
    }

    @Override
    //MODIFIES: savedQueue.txt
    //EFFECTS: insert the playlist to the savedQueue.txt
    public void insert(){
        String fileLocation =  "src/savedFiles/savedQueue.txt";
        File queueFile = new File (fileLocation);

        if (!queueFile.exists()) {
            try {
                File directory = new File(queueFile.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                queueFile.createNewFile();

            } catch (IOException e) {
                log("Exception Occured: " + e.toString());
            }
        }

        try {
            // Convenience Class for Writing character files
            FileWriter playlistWriter;
            playlistWriter = new FileWriter(queueFile.getAbsoluteFile(), true);

            //Write text
            BufferedWriter bufferedWriter = new BufferedWriter(playlistWriter);
            for (Song song: getListOfSongs()){
                bufferedWriter.write("\n"+"-"+ song.getSongName());
            }
            bufferedWriter.close();

            log("queueFile data saved at file location: " + fileLocation + "\n" + "Data: " + getPlayListName() + "\n");

        } catch (IOException e) {
            log("Hmmmm.. got an error while saving Playlist Data to file " + e.toString());
        }
    }


    //REQUIRES: myData needs to be in the format of gson.toJson(object)
    //EFFECTS: write to playlistName.txt. Create a new playlistName.txt if not found.
    public void writeToFile(String myData) throws IOException{
        fileLocation = "src/savedFiles/savedPlaylists/"+ getPlayListName() +".txt";

        //Open or Create savedPlaylist.txt under fileLocation
        File playlistFile = new File(fileLocation);
        if (!playlistFile.exists()) {
            try {
                File directory = new File(playlistFile.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                playlistFile.createNewFile();

            } catch (IOException e) {
                log("Exception Occured: " + e.toString());
            }
        }

        //Clear out the content original file
        PrintWriter writer = new PrintWriter(playlistFile);
        writer.print("");
        writer.close();

        //write MyData into the savedPlaylist.txt
        try {
            // Convenience Class for Writing character files
            FileWriter playlistWriter;
            playlistWriter = new FileWriter(playlistFile.getAbsoluteFile(), true);

            //Write text
            BufferedWriter bufferedWriter = new BufferedWriter(playlistWriter);
            bufferedWriter.write(myData.toString());
            bufferedWriter.close();

            log("Playlist data saved at file location: " + fileLocation + "\n" + "Data: " + myData + "\n");

        } catch (IOException e) {
            log("Hmmmm.. got an error while saving Playlist Data to file " + e.toString());
        }

    }


    //EFFECTS: read from savedPlaylist.txt
    public void readFromFile(String fileLocation) {
        File playlistFile = new File(fileLocation);
        if (!playlistFile.exists()) {
            log("File does not exist");
        }

        InputStreamReader isReader;
        try {
            isReader = new InputStreamReader(new FileInputStream(playlistFile), "UTF-8");
            JsonReader myReader = new JsonReader(isReader);
            Playlist playlist = gson.fromJson(myReader, Playlist.class);

            playListName = playlist.getPlayListName();
            listOfSongs = playlist.getListOfSongs();

            log("Playlist Name: " + playlist.getPlayListName());
            playlist.printPlayList();

        } catch (Exception e) {
            log("error load cache from file " + e.toString());
        }

        log("\n"+"Playlist Data loaded successfully from file " + "\n" + fileLocation);

    }


}


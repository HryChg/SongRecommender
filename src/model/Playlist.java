package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import exceptions.AlreadyInPlaylistException;
import exceptions.EmptyPlaylistException;
import exceptions.EmptyStringException;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class Playlist extends ReadableWritable implements Queueable, Printable{
    private String playListName;
    private ArrayList<Song> listOfSongs;
    private HashMap<Song, Timestamp> songAddDates; //each song is associated with an add date to the playlist

    private static Gson gson = new Gson(); //used for reading and writing to savedPlayList

    public Playlist(String name) {
        this.playListName = name;
        this.listOfSongs = new ArrayList<>();
        this.songAddDates = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: Setting the Playlist Name. Throw EmptyStringException if playlistName is empty string
    public void setPlaylistName(String playlistName) throws EmptyStringException {
        if (playlistName.equals(""))
            throw new EmptyStringException();
        this.playListName = playlistName;
    }

    //GETTERS
    public String getPlayListName() {
        return this.playListName;
    }

    public ArrayList<Song> getListOfSongs() {
        return this.listOfSongs;
    }

    public HashMap<Song, Timestamp> getSongAddDates() {
        return songAddDates;
    }

    public int getSize() {
        return this.listOfSongs.size();
    }

    //EFFECTS: return song at specified index in playlist. Return null if the index is not in the range of playlist
    public Song getSong(int location) {
        if ((0 <= location) && (location <= (this.getSize() - 1))) {
            return this.getListOfSongs().get(location);
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: add Song to the playlist
    //     throw AlreadyInPlaylistException if song already exists in playlist
    //     throw NullPointerException if the song is null
    public void addSong(Song song) throws AlreadyInPlaylistException {
        if (song == null)
            throw new NullPointerException();

        if (this.listOfSongs.contains(song)) {
            throw new AlreadyInPlaylistException();
        } else {
            this.listOfSongs.add(song);
            this.songAddDates.put(song, new Timestamp(new Date().getTime()));
            song.addAssociatedPlaylist(this);
            //System.out.println(song.getSongName()+" added.");
        }
    }


    //EFFECTS: return true if the playlist contain song
    public boolean contains(Song song) {
        for (Song s : listOfSongs) {
            if (s.equals(song))
                return true;
        }

        return false;
    }

    @Override
    //EFFECTS: print out the names of all songs in the playlist.
    public void print() {
        System.out.println("Current Playlist: " + this.getPlayListName());
        for (Song s : listOfSongs) {
            System.out.println("- " + s.getSongName());
        }

    }

    @Override
    //MODIFIES: savedQueue.txt
    //EFFECTS: insert the playlist to savedQueue.txt
    public void insertQueue() {
        String fileLocation = "savedFiles/savedQueue.txt";
        File queueFile = new File(fileLocation);

        //create a new file if it does not exist under fileLocation
        if (!queueFile.exists()) {
            try {
                File directory = new File(queueFile.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                queueFile.createNewFile();

            } catch (IOException e) {
                System.out.println("Exception Occurred: " + e.toString());
            }
        }

        //trying to add content into the queue
        try {
            // Convenience Class for Writing character files
            FileWriter playlistWriter;
            playlistWriter = new FileWriter(queueFile.getAbsoluteFile(), true);

            //Write text
            BufferedWriter bufferedWriter = new BufferedWriter(playlistWriter);
            for (Song song : getListOfSongs()) {
                bufferedWriter.write("\n" + "- " + song.getSongName());
            }
            bufferedWriter.close();

            System.out.println("queueFile saved at location: " + fileLocation + "\n" + "Data added: " + getPlayListName() + "\n");

        } catch (FileNotFoundException e) {
            System.out.println("Error! File not Found!");
        } catch (IOException e) {
            System.out.println("Hmm... got an error while saving Playlist Data to file " + e.toString());
        }
    }

    @Override
    //REQUIRES: myData needs to be in the format of GSON string
    //EFFECTS: save myData to a file under savedFiles/savedPlaylists with playlistName as fileName
    //    throws EmptyPlaylistException is the playlist is empty
    public void writeToFile(String myData) throws EmptyPlaylistException {
        if (this.getSize() == 0)
            throw new EmptyPlaylistException();
        String fileLocation = "savedFiles/savedPlaylists/" + getPlayListName() + ".txt";
        File playlistFile = super.generateFileAndDirectoryIfNotThere(fileLocation);
        super.writeFile(myData, playlistFile);
    }

    @Override
    //MODIFIES: this
    //EFFECTS: open a file from fileLocation and load it to this
    public void readFromFile(String fileLocation) {
        try {
            File playlistFile = super.getFile(fileLocation);
            Playlist extractedPlaylist = convertFileToPlaylist(playlistFile);
            playListName = extractedPlaylist.getPlayListName();
            listOfSongs = extractedPlaylist.getListOfSongs();
        } catch (Exception e) {
            super.printLoadingErrorMessage(e);
        }
        super.printLoadingSuccessMessage(fileLocation);
    }

    private Playlist convertFileToPlaylist(File playlistFile) throws UnsupportedEncodingException, FileNotFoundException {
        InputStreamReader isReader = new InputStreamReader(new FileInputStream(playlistFile), "UTF-8");
        JsonReader myReader = new JsonReader(isReader);
        return gson.fromJson(myReader, Playlist.class);
    }

    @Override
    //EFFECTS: return true if two playlist have the same name and same order of songs.
    public boolean equals(Object playlist) {
        if (this == playlist)
            return true;

        if (playlist == null || this.getClass() != playlist.getClass()) {
            return false;
        }

        //Compare playlist name
        Playlist castedPlaylist = (Playlist) playlist;
        if (this.getPlayListName() != castedPlaylist.getPlayListName())
            return false;

        //Compare playlist listOfSongs
        ArrayList<Song> songsToBeCompared = castedPlaylist.getListOfSongs();
        ArrayList<Song> originalSongs = this.getListOfSongs();
        //if two playlist has same songs but different order, this will return false
        if (!originalSongs.equals(songsToBeCompared))
            return false;

        return true;
    }

    @Override
    //EFFECTS: generate hashCode of a playlist.
    public int hashCode() {
        //generated by the songs it contains and the playlist name
        //Same playlist name but different song order will lead to different hashCode
        int hashCode = playListName.hashCode();
        for (Song song : listOfSongs) {
            hashCode = 31 * hashCode + (song == null ? 0 : song.hashCode());
        }
        for (Song song : songAddDates.keySet()) {
            hashCode = 31 * hashCode + (songAddDates.get(song) == null ? 0 : songAddDates.get(song).hashCode());
        }
        return hashCode;
    }


}


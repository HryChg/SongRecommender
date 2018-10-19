package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import exceptions.AlreadyInPlaylistException;
import exceptions.EmptyException;
import exceptions.EmptyPlaylistException;
import exceptions.EmptyStringException;

import java.io.*;
import java.util.ArrayList;

import static org.apache.pdfbox.util.ErrorLogger.log;

public class Playlist extends AbstractReadableWritable implements Queueable {
    private String playListName;
    private ArrayList<Song> listOfSongs = new ArrayList<Song>();
    private static String fileLocation;
    private static Gson gson = new Gson(); //used for reading and writing to savedPlayList

    public Playlist(String name) {
        this.playListName = name;
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
        if (this.listOfSongs.contains(song))
            throw new AlreadyInPlaylistException();
        if (song == null)
            throw new NullPointerException();

        this.listOfSongs.add(song);

    }

    //EFFECTS: return true if the playlist contain song
    public boolean contains(Song song) {
        return this.listOfSongs.contains(song);
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
    //REQUIRES: myData needs to be in the format of gson.toJson(object)
    //EFFECTS: write to playlistName.txt. Create a new playlistName.txt if not found.
    //    throws EmptyPlaylistException is the playlist is empty
    public void writeToFile(String myData) throws EmptyPlaylistException {
        if (this.getSize() == 0)
            throw new EmptyPlaylistException();

        fileLocation = "savedFiles/savedPlaylists/" + getPlayListName() + ".txt";

        //Open or Create playlist under fileLocation
        File playlistFile = new File(fileLocation);
        if (!playlistFile.exists()) {
            try {
                File directory = new File(playlistFile.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                playlistFile.createNewFile();

            } catch (IOException e) {
                System.out.println("Exception Occurred: " + e.toString());
            }
        }

        //Clear out the content original file
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(playlistFile);
        } catch (FileNotFoundException e) {
            System.out.println("Exception Occurred: " + e.toString());
        }
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

            System.out.println("playlistFile saved at location: " + fileLocation + "\n" + "Data added: " + myData + "\n");

        } catch (IOException e) {
            System.out.println("Hmm... got an error while saving Playlist Data to file " + e.toString());
        }

    }


    @Override
    //EFFECTS: read from savedPlaylist.txt
    public void readFromFile(String fileLocation) {
        File playlistFile = new File(fileLocation);
        if (!playlistFile.exists()) {
            log("The files does not exists.");
        }

        InputStreamReader isReader;
        try {
            isReader = new InputStreamReader(new FileInputStream(playlistFile), "UTF-8");
            JsonReader myReader = new JsonReader(isReader);
            Playlist extractedPlaylist = gson.fromJson(myReader, Playlist.class);

            playListName = extractedPlaylist.getPlayListName();
            listOfSongs = extractedPlaylist.getListOfSongs();

            System.out.println("Playlist Name Loaded: " + extractedPlaylist.getPlayListName());
            extractedPlaylist.print();

        } catch (Exception e) {
            System.out.println("error load cache from file " + e.toString());
        }

        System.out.println("\n" + "Playlist Data loaded successfully from location: " + "\n" + fileLocation);
    }


}


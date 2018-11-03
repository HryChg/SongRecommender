package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import exceptions.AlreadyInPlaylistException;
import exceptions.EmptyStringException;


import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Song extends ReadableWritable implements Printable, Queueable {
    private String songName;
    private Boolean isFavorite = false;
    private Boolean isHate = false;   // whether user hates the song
    private Timestamp lastPlayedDate; // the date when the song is most recently played
    private Timestamp playedTime;     // the time of the day when song is played


    private transient Set<Playlist> associatedPlaylists; //associated Playlist

    private static Gson gson = new Gson(); //used to create or read files

    //CONSTRUCTORS
    public Song(String name) {
        this.songName = name;
        this.associatedPlaylists = new HashSet<>();
    }

    //SETTERS
    //EFFECTS: Set the songName to name. Throw EmptyStringException is name is empty string.
    public void setSongName(String name) throws EmptyStringException {
        if (name.equals(""))
            throw new EmptyStringException();
        this.songName = name;
    }

    public void setIsFavorite(Boolean favorite) {
        this.isFavorite = favorite;
    }

    public void setIsHate(Boolean hate) {
        this.isHate = hate;
    }

    public void setLastPlayedDate(Timestamp date) {
        this.lastPlayedDate = date;
    }

    public void setPlayedTime(Timestamp timing) {
        this.playedTime = timing;
    }


    //GETTERS
    public String getSongName() {
        return songName;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public Boolean getIsHate() {
        return isHate;
    }

    public Timestamp getLastPlayedDate() {
        return lastPlayedDate;
    }

    public Timestamp getPlayedTime() {
        return playedTime;
    }

    public Set<Playlist> getAssociatedPlaylists() {
        return associatedPlaylists;
    }


    //EFFECTS: return the last played date of the song in String Format (DayInWeek Year Month Day);
    //         it also print out statements indicating that date
    public String printLastPlayedDate() {
        String style = "E yyyy.MM.dd";
        String result = formatDateTime(style, this.lastPlayedDate);
        System.out.println("Last Played Date: " + result);
        return result;
    }

    //EFFECTS: return the first played time of the song in String Format (Hour:Minute:Second);
    //         it also print out statement indicating that time
    public String printPlayedTime() {
        String style = "kk:mm:ss";
        String result = formatDateTime(style, this.playedTime);
        System.out.println("Last Played Time: " + result);
        return result;
    }

    private String formatDateTime(String style, Timestamp timestamp) {
        SimpleDateFormat ft = new SimpleDateFormat(style);
        return ft.format(timestamp);
    }


    @Override
    // EFFECTS: print out all information stored under this song
    public void print() {
        System.out.println("Song Name: " + songName);
        System.out.println("Favorite:  " + isFavorite.toString());
        System.out.println("Hate:      " + isHate.toString());
        try {
            printLastPlayedDate();
            printPlayedTime();
        } catch (NullPointerException e) {
            System.out.println("Warning: LastPlayedDate or PlayedTime is null.");
        }
    }

    @Override
    // MODIFIES: savedQueue.txt
    // EFFECTS: insertQueue the song into the savedQueue.txt
    public void insertQueue() {
        //TODO, come back and clean this up
        String fileLocation = "savedFiles/savedQueue.txt";
        File queueFile = new File(fileLocation);

        // Create a file if it does not exists
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

        try {
            // Convenience Class for Writing character files
            FileWriter songWriter;
            songWriter = new FileWriter(queueFile.getAbsoluteFile(), true);

            //Write text
            BufferedWriter bufferedWriter = new BufferedWriter(songWriter);
            bufferedWriter.write("\n" + "- " + getSongName());
            bufferedWriter.close();

            System.out.println("queueFile data saved at file location: " + fileLocation + "\n" + "Data Added: " + getSongName() + "\n");

        } catch (FileNotFoundException e) {
            System.out.println("Hey! File Not Found!");
        } catch (IOException e) {
            System.out.println("Hmm... got an error while saving inserting song ot queueFile: " + e.toString());
        }

    }

    @Override
    //REQUIRES: myData needs to be in the format of GSON string
    //EFFECTS: save myData to a file under savedFiles/savedSongs with songName as fileName
    public void writeToFile(String myData) {
        String fileLocation = "savedFiles/savedSongs/" + getSongName() + ".txt";
        File songFile = super.generateFileAndDirectoryIfNotThere(fileLocation);
        super.writeFile(myData, songFile);
    }

    @Override
    //MODIFIES: this
    //EFFECTS: open a file from fileLocation and load it to this
    public void readFromFile(String fileLocation) {
        try {
            File songFile = super.getFile(fileLocation);
            Song extractedSong = convertFileToSong(songFile);
            songName = extractedSong.getSongName();
            isFavorite = extractedSong.getIsFavorite();
            isHate = extractedSong.getIsHate();
            lastPlayedDate = extractedSong.getLastPlayedDate();
            playedTime = extractedSong.getPlayedTime();
        } catch (Exception e) {
            super.printLoadingErrorMessage(e);
        }

        super.printLoadingSuccessMessage(fileLocation);
    }

    //TODO, how can I rase this to the ReadableWritable Class?
    private Song convertFileToSong(File songFile) throws UnsupportedEncodingException, FileNotFoundException {
        InputStreamReader isReader = new InputStreamReader(new FileInputStream(songFile), "UTF-8");
        JsonReader myReader = new JsonReader(isReader);
        return gson.fromJson(myReader, Song.class);
    }

    @Override
    public boolean equals(Object song) {
        if (this == song)
            return true;

        if (song == null || this.getClass() != song.getClass())
            return false;

        Song castedSong = (Song) song;
        return this.songName.equals(castedSong.songName);
    }

    @Override
    //EFFECTS: return hashCode of the song based on songName
    public int hashCode() {
        return Objects.hashCode(songName);
    }

    public void addAssociatedPlaylist(Playlist playlist) {

        if (!associatedPlaylists.contains(playlist)) {
            associatedPlaylists.add(playlist);
            try {
                playlist.addSong(this);
            } catch (AlreadyInPlaylistException e) {
                //do nothing
            }
        }
    }


}

package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import exceptions.EmptyException;
import exceptions.EmptyStringException;


import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.pdfbox.util.ErrorLogger.log;


public class Song extends AbstractReadableWritable implements Printable, Queueable {
    private String songName;
    private Boolean isFavorite = false;
    private Boolean isHate = false;   // whether user hates the song
    private Timestamp lastPlayedDate; // the date when the song is most recently played
    private Timestamp playedTime; // the time of the day when song is played

    private String fileLocation;
    private static Gson gson = new Gson(); //used to create or read files

    //CONSTRUCTORS
    public Song(String name) {
        this.songName = name;
    }

    //SETTERS
    //EFFECTS: Set the songName to name. Throw EmptyStringException is name is empty string.
    public void setSongName(String name) throws EmptyStringException {
        if (name == "")
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


    //EFFECTS: return the last played date of the song in String Format (DayInWeek Year Month Day);
    //         it also print out statements indicating that date
    public String printLastPlayedDate() {
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd");
        System.out.println("Last Played Date: " + ft.format(this.lastPlayedDate));
        return ft.format(this.lastPlayedDate);
    }

    //EFFECTS: return the first played time of the song in String Format (Hour:Minute:Second);
    //         it also print out statement indicating that time
    public String printPlayedTime() {
        SimpleDateFormat ft = new SimpleDateFormat("kk:mm:ss");
        System.out.println("Last Played Time: " + ft.format(this.playedTime));
        return ft.format(this.playedTime);
    }

    @Override
    // EFFECTS: print out all information stored under this song
    public void print() {
        List<String> dataToPrint = new ArrayList<>();
        dataToPrint.add("Song Name: " + songName);
        dataToPrint.add("Favorite:  " + isFavorite.toString());
        dataToPrint.add("Hate:      " + isHate.toString());

        for (String s : dataToPrint) {
            System.out.println(s);
        }

        try {
            printLastPlayedDate();
            printPlayedTime();

            dataToPrint.add(getLastPlayedDate().toString());
            dataToPrint.add(getPlayedTime().toString());
        } catch (NullPointerException e) {
            System.out.println("Error: lastPlayedDate and playedTime are null");
        }


    }

    @Override
    // MODIFIES: savedQueue.txt
    // EFFECTS: insertQueue the song into the savedQueue.txt
    public void insertQueue() {
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

            }
            catch (IOException e) {
                System.out.println("Exception Occured: " + e.toString());
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


    //EFFECTS: open songName.txt
    @Override
    public void writeToFile(String myData) {


        fileLocation = "savedFiles/savedSongs/" + getSongName() + ".txt";
        File songFile = new File(fileLocation);
        if (!songFile.exists()) {
            try {
                File directory = new File(songFile.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                songFile.createNewFile();
            } catch (IOException e) {
                log("Exception Occurred: " + e.toString());
            }
        }

        try {
            //Convenience Class for writing character files
            FileWriter songWriter;
            songWriter = new FileWriter(songFile.getAbsoluteFile(), true);

            //Clear out the content original file
            PrintWriter writer = new PrintWriter(songFile);
            writer.print("");
            writer.close();

            //Writes text to a character-output stream
            BufferedWriter bufferedWriter = new BufferedWriter(songWriter);
            bufferedWriter.write(myData.toString());
            bufferedWriter.close();

            log("Song data saved at file location: " + "\n" + fileLocation);
        } catch (IOException e) {
            log("Hmm... Got an erroR while saving Song data to file " + e.toString());
        }

    }

    @Override
    public void readFromFile(String fileLocation) {
        File songFile = new File(fileLocation);
        if (!songFile.exists()) {
            System.out.println("File doesn't exist");
        }

        InputStreamReader isReader;
        try {
            //We are unloading the saved file back to current object
            isReader = new InputStreamReader(new FileInputStream(songFile), "UTF-8");
            JsonReader myReader = new JsonReader(isReader);
            Song extractedSong = gson.fromJson(myReader, Song.class);

            songName = extractedSong.getSongName();
            isFavorite = extractedSong.getIsFavorite();
            isHate = extractedSong.getIsHate();
            lastPlayedDate = extractedSong.getLastPlayedDate();
            playedTime = extractedSong.getPlayedTime();

            System.out.println("Song Name: " + extractedSong.getSongName() + " is loaded");


        } catch (Exception e) {

            log("error loading cache from file: " + e.toString());
        }

        System.out.println("Song data loaded successfully from location:" + "\n" + fileLocation);

    }


}

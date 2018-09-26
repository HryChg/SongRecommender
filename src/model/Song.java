package model;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Song {
    private String songName;
    private Boolean isFavorite = false;
    private Boolean isHate = false;   // whether user hates the song
    private Timestamp lastPlayedDate; // the date when the song is most recently played
    private Timestamp PlayedTime; // the time of the day when song is played


    public Song(String name) {
        this.songName = name;
    }

    //MODIFIES: this
    //EFFECTS: set the song name
    public void setSongName(String name) {
        this.songName = name;
    }

    //MODIFIES: this
    //EFFECTS: set IsFavorite. True if favorite. False otherwise.
    public void setIsFavorite(Boolean favorite) {
        this.isFavorite = favorite;
    }

    //MODIFIES: this
    //EFFECTS: set isHate. True if hate the song, false otherwise
    public void setIsHate(Boolean hate) {
        this.isHate = hate;
    }

    //MODIFIES: this
    //EFFECTS: set the last played date of a song
    public void setLastPlayedDate(Timestamp date) {
        this.lastPlayedDate = date;
    }

    //MODIFIES: this
    //EFFECTS: set first played time of the song
    public void setPlayedTime(Timestamp timing) {
        this.PlayedTime = timing;
    }


    //EFFECTS: get the song name
    public String getSongName() {
        return songName;
    }

    //EFFECTS: get the status of isFavorite
    public Boolean getIsFavorite() {
        return isFavorite;
    }

    //EFFECTS: get the status of isHate
    public Boolean getIsHate() {
        return isHate;
    }

    //EFFECTS: get the last played date of the song
    public Timestamp getLastPlayedDate() {
        return lastPlayedDate;
    }

    //EFFETCS: get the first played time the song
    public Timestamp getPlayedTime() {
        return PlayedTime;
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
        System.out.println("Last Played Time: " + ft.format(this.PlayedTime));
        return ft.format(this.PlayedTime);
    }

}

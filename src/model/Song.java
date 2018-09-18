package model;

import java.sql.Time;
import java.util.Date;

public class Song {
    private String  songName;
    private String  Genre;
    private Boolean isFavorite;
    private Boolean isHate;         // whether user hates the song
    private Date    recentlyPlayed; // the date when the song is most recently played
    private Time    timing;         // the time of the day when song is played

    // CONSTRUCTOR
    public void Song(){}


    // SETTER
    public void setSongName(String name){
        this.songName = name;
    }


    // GETTER
    public String getSongName(){
        return songName;
    }

    public String getGenre() {
        return Genre;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public Boolean getHate() {
        return isHate;
    }

    public Date getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public Time getTiming() {
        return timing;
    }


    // For Printing the SongName when simply print(ArrayList<Song>())
    public String toString(){
        return (songName);
    }

}

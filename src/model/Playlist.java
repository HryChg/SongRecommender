package model;

import java.util.ArrayList;

public class Playlist {
    private String playListName;
    private ArrayList<Song> listOfSongs = new ArrayList<Song>();

    public Playlist() {}


    // MODIFIES: this
    // EFFECTS: Setting the Playlist Name
    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    //EFFECTS: getting the playlist name
    public String getPlayListName() {
        return this.playListName;
    }

    //EFFECTS: get a list of songs current stored in playlist
    public ArrayList<Song> getListOfSongs() {
        return this.listOfSongs;
    }

    //EFFECTS: getting the size of a playlist
    public int getSize() {
        return this.listOfSongs.size();
    }

    //EFFETCS: getting song in the playlist at specified index. Return null if the index is not in the range of playlist
    public Song getSong(int location){
        if ((0<= location) && (location <= (this.getSize()-1))) {
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
    public boolean contains(Song song){
        if (this.listOfSongs.contains(song)){
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


}


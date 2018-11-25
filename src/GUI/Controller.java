package GUI;

import exceptions.EmptyPlaylistException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Playlist;
import model.PlaylistManager;
import model.Song;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Playlist dataBase = new Playlist("database");
    private Playlist currentQueue = new Playlist("currentQueue");
    private PlaylistManager pm = new PlaylistManager();

    @FXML private ListView<Song> songListView = new ListView<>();
    @FXML private Label status;
    @FXML private Button button;
    @FXML private Button pauseButton;
    @FXML private Button skipButton;
    @FXML private Button stopButton;

    @FXML private CheckBox favoriteBox;
    @FXML private CheckBox hateBox;
    @FXML private CheckBox recentlyPlayedBox;
    @FXML private CheckBox lostSongBox;
    @FXML private CheckBox neverPlayedBox;
    @FXML private CheckBox allSongsBox;

    @FXML private Button submitButton;



    //this is where you run your initialization when the window first open
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initializing songList
        System.out.println("Loading DataBase...");

        dataBase.readFromFile("/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/savedFiles/savedPlaylists/database.txt");
        songListView.getItems().addAll(dataBase.getListOfSongs());

        //initializing status bar
        status.setText("Loaded Playlist");
    }


    public void playButtonClick(){
        status.setText("Play Button Clicked.");
        //todo play music here
    }

    public void pauseButtonClick(){
        status.setText("Pause Button Clicked.");
        //todo: pause music here
    }

    public void skipButtonClick(){
        status.setText("Skip Button Clicked.");
        //todo: skip music here
    }

    public void stopButtonClick(){
        status.setText("Stop Button Clicked");
        //todo: stop muisc here
    }

    public void submitButtonClick(){
        status.setText("New Selections Made...");
        handleOptions(favoriteBox, hateBox, recentlyPlayedBox, lostSongBox, neverPlayedBox, allSongsBox);
    }

    //add filters on database base on the selected choiceBox
    private void handleOptions(CheckBox favoriteBox, CheckBox hateBox, CheckBox recentlyPlayedBox, CheckBox lostSongBox, CheckBox neverPlayedBox, CheckBox allSongsBox){

        currentQueue = pm.filter(dataBase, favoriteBox.isSelected(), hateBox.isSelected(),
                recentlyPlayedBox.isSelected(), lostSongBox.isSelected(), neverPlayedBox.isSelected(), allSongsBox.isSelected());
    }



    //todo update the database in the end
    //before exiting, update the data base
    public void exitProcedure(){

        try {
            dataBase.writeToFile(dataBase.convertToGsonString());
        } catch (EmptyPlaylistException e){
            e.printStackTrace();
        }
    }
}
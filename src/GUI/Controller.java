package GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.MusicPlayer.MusicPlayer;
import model.Playlist;
import model.PlaylistManager;
import model.Song;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Playlist dataBase = new Playlist("database");
    private Playlist currentQueue = new Playlist("currentQueue");
    private PlaylistManager pm = new PlaylistManager();
    private MusicPlayer musicPlayer = MusicPlayer.getInstance();
    private boolean musicPlayerInitialized = false;


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

        System.out.println("The following is the current data base:");
        dataBase.print();
        System.out.println("\n");



        System.out.println("The following is the current music player queue:");
        musicPlayer.getPlaylist().print();
        System.out.println("\n");


    }


    public void playButtonClick(){
        status.setText("Play Button Clicked.");


        if(musicPlayer.getPlaylist().getListOfSongs().isEmpty()){
            status.setText("Current Queue is empty. Please select check box and hit submit button!");
            return;
        }


        if (!musicPlayerInitialized){
            musicPlayer.initializeThreadAndPlay();
            musicPlayerInitialized = true;
        } else{
            musicPlayer.resume();
        }
    }

    public void pauseButtonClick(){
        status.setText("Pause Button Clicked.");
        musicPlayer.pause();
    }

    public void skipButtonClick(){
        status.setText("Skip Button Clicked.");
        musicPlayer.skip();
    }

    public void stopButtonClick(){
        status.setText("Stop Button Clicked");
        //todo: stop muisc here
        musicPlayer.stop();
        musicPlayer = MusicPlayer.refreshInstance();
        musicPlayer.setPlaylist(currentQueue);
        musicPlayerInitialized = false;
    }

    public void submitButtonClick(){
        status.setText("New Selections Made...");
        handleOptions(favoriteBox, hateBox, recentlyPlayedBox, lostSongBox, neverPlayedBox, allSongsBox);
    }

    //add filters on database base on the selected choiceBox
    private void handleOptions(CheckBox favoriteBox, CheckBox hateBox, CheckBox recentlyPlayedBox, CheckBox lostSongBox, CheckBox neverPlayedBox, CheckBox allSongsBox){

        currentQueue = pm.filter(dataBase, favoriteBox.isSelected(), hateBox.isSelected(),
                recentlyPlayedBox.isSelected(), lostSongBox.isSelected(), neverPlayedBox.isSelected(), allSongsBox.isSelected());

        currentQueue.print();
        musicPlayer.setPlaylist(currentQueue);
    }

    //save the database before exiting
    //being called in Main class
    public void exitProcedure(){
        //todo comeback and tne turn this saving mode on
//        try {
//            System.out.println("Saving to database...");
//            dataBase.writeToFile(dataBase.convertToGsonString());
//        } catch (EmptyPlaylistException e){
//            e.printStackTrace();
//        }
    }
}
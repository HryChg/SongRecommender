package GUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Playlist;
import model.PlaylistManager;
import model.Song;

import java.io.File;
import java.util.List;


public class JavafxSample extends Application {
    private int WIDTH = 600;
    private int HEIGHT = 400;
    private int TITLE_FONT = 20;


    @Override
    public void start(Stage primaryStage) throws Exception {

        /*---------------------------
        * The Title  on the Scene
        * ---------------------------*/
        Text text = new Text();
        text.setFont(new Font(TITLE_FONT));
        text.setX((WIDTH / 3) * 1.6);
        text.setY(30);
        text.setText("Song Recommender");


        /*---------------------------
         * line divider between songs and panels
         * ---------------------------*/
        Line line = new Line();
        line.setStartX(WIDTH / 3);
        line.setStartY(0);
        line.setEndX(WIDTH / 3);
        line.setEndY(HEIGHT);


        /*---------------------------
         * BUTTONS
         *---------------------------*/
        //recently played playlist Buttons
        Button recentlyPlayedButton = createButton("Recently Played", 220.00, 100.00);
        recentlyPlayedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //once clicked, do sth in here
                recentlyPlayedButton.setText("Clicked!");
            }
        });

        //surprise me button
        Button SurpriseMeButton = createButton("Surprise me!", 220.00, 150.00);
        SurpriseMeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //once clicked, do sth in here
                SurpriseMeButton.setText("Clicked!");
            }
        });

        //long lost song
        Button button3 = createButton("long lost Song", 220.00, 200.00);
        button3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //once clicked, do sth in here
                button3.setText("Clicked!");
            }
        });

//        //mood of the day
//        Button button4 = createButton("Mood Of The Day!", 220.00, 250.00);
//        button4.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                //once clicked, do sth in here
//                button4.setText("Clicked!");
//            }
//        });

        //Play Button
        Button playButton = createButton("Play", 220.00, 350.00);
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                playButton.setText("Clicked!");
            }
        });


        //Pause Button
        Button pauseButton = createButton("Pause", 320.00, 350.00);
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //once clicked, do sth in here
                pauseButton.setText("Clicked!");
            }
        });


        //Skip Button
        Button skipButton = createButton("Skip", 420.00, 350.00);
        skipButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //once clicked, do sth in here
                skipButton.setText("Clicked!");
            }
        });

        //Restart Button
        Button restartButton = createButton("Restart", 520.00, 350.00);
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //once clicked, do sth in here
                restartButton.setText("Clicked!");
            }
        });



        /*---------------------------
         * LEFT COLUMN
         *---------------------------*/
        //TODO: 1. Click songs name to play that song
        //List<Song> list = getAllFiles();
        List<Song> list = getDataBase();

        ObservableList<Song> allSongs = FXCollections.observableArrayList(list);
        ListView<Song> listView = new ListView<>();
        listView.setItems(allSongs);
        listView.setMaxWidth(200);




        /*---------------------------
         * JAVA FX Media Player
         *---------------------------*/
        //Java FX Media PLayer
        //TODO: HELP!!!! Media PLayer is not playing sound. I am almost certain that there is sth wrong with my path format

        String path = "resources/Azrael.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

//        try {
//            String path = new File("songs/Azrael.mp3").getAbsolutePath();
//            Media me = new Media(new File(path).toURI().toString());
//
//            MediaPlayer mp = new MediaPlayer(me);
//            mp.play();
//        } catch (Exception e) {
//            System.out.println(e.getCause());
//        }


        /*---------------------------
         * MediaView on Current Song being played
         *---------------------------*/
        //TODO: Build the Current Playing Song View probabliy something with a media view




        /*---------------------------
         * GROUPING ELEMENTS and START SCENE
         *---------------------------*/
        Group root = new Group(line, text, recentlyPlayedButton, SurpriseMeButton, button3,
                listView, playButton, pauseButton, skipButton, restartButton);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Song Recommender");
        scene.setFill(Color.BROWN);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    //MAIN
    public static void main(String args[]) {
        launch(args);
    }







    /*---------------------------
     * HELPERS
     *---------------------------*/
    private Button createButton(String name, Double x, Double y) {
        Button b = new Button(name);
        b.setLayoutX(x);
        b.setLayoutY(y);
        return b;
    }

    //EFFECTS: open multiple fresh new files
    private List<Song> getAllFiles() {
        PlaylistManager pm = new PlaylistManager();
        Playlist p = new Playlist("");
        pm.saveMultipleAudioFilesToPlaylist("songs", p);
        return p.getListOfSongs();
    }

    //EFFECTS: open existing song database
    private List<Song> getDataBase(){
        Playlist p = new Playlist("Song Database");
        p.readFromFile("/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/savedFiles/savedPlaylists/MainPlaylist.txt");
        return p.getListOfSongs();
    }


}
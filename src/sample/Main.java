package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    //String path = "/Users/harrychuang/Downloads/mpthreetest.mp3";
    String path = "/Users/harrychuang/Desktop/CPSC 210/CSPC 210 Personal Course Project/GitHub Repo/projectw1_team997/songs/Death.mp3";




    @Override
    public void start(Stage primaryStage) throws Exception{



        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

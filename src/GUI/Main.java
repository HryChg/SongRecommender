package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Stage window;


    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        // this tells Java to use our closing procedure
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });


        Parent root = FXMLLoader.load(getClass().getResource("MainFrame.fxml"));
        window.setTitle("Hello World");
        window.setScene(new Scene(root, 800, 500));
        window.show();
    }

    private void closeProgram(){

    }


    public static void main(String[] args) {
        launch(args);
    }
}

package Controllers;

import Shared.Request;
import Shared.Response;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.rmi.UnknownHostException;
import java.sql.SQLException;

public class MainController extends Application {
    public void switchToLogin(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/logged-in.fxml");
    }
    public void switchToSignUp(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/signUp.fxml");
    }

    @Override
    public void start(Stage stage) {
        try {
            URL url = new File("C:\\Users\\astan\\Downloads\\Spotify-Develop (2)\\Spotify-Develop\\Spotify\\src\\main\\resources\\main.fxml").toURI().toURL();

            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            stage.setTitle("Spotify!");

            Image icon = new Image(new FileInputStream("C:\\Users\\astan\\spotify\\src\\images\\spotifyIcon.png"));
            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
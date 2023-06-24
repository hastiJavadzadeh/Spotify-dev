package Controllers;

import Shared.Request;
import Shared.Response;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static Controllers.LoggedInController.user;

public class SearchSongResultController {

    @FXML
    private Label songLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private Label albumLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Label popularityLabel;
    @FXML
    private Label releaseDateLabel;

    private static int trackID;

    private Media media;
    private MediaPlayer mediaPlayer;

    private Timer timer;
    private TimerTask task;
    private boolean running;


    public void exit(ActionEvent event) throws IOException{
        mediaPlayer.pause();
        //TODO
        Controller.changeScene(event, "/welcome-page.fxml");
    }
    public void info(String path, String title, String artist, String album, String genre, double popularity, String releaseDate, int ID){
        songLabel.setText(title);
        artistLabel.setText(artist);
        albumLabel.setText(album);
        genreLabel.setText(genre);
        popularityLabel.setText(String.valueOf(popularity));
        releaseDateLabel.setText(releaseDate);
        trackID = ID;

        try {
            Socket socket = new Socket("127.0.0.1", 2345);
            System.out.println("Connected to server!");
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(output, true);
            Request request = new Request();
            Response response = new Response();

            JSONObject json=new JSONObject();
            json.put("Command","Play song");
            json.put("Title",title);
            json.put("Artist",artist);
            request.setJson(json);

            out.println(request.getJson().toString());
            response.setJson(new JSONObject(in.readLine()));

            while (response.getJson() != null) {

                media = new Media(new File(path).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                break;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
    public void play() {
        mediaPlayer.play();
    }
    public void pause() {
        mediaPlayer.pause();
    }

    public void beginTimer() {

    }
    public void cancelTimer() {

    }

    public void like() {
        try {
            Socket socket = new Socket("127.0.0.1", 2345);
            System.out.println("Connected to server!");
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(output, true);
            Request request = new Request();
            Response response = new Response();

            JSONObject json=new JSONObject();
            json.put("Command","Like");
            json.put("trackID",trackID);
            request.setJson(json);

            out.println(request.getJson().toString());
            response.setJson(new JSONObject(in.readLine()));

            while (response.getJson() != null) {
                JSONObject resp=response.getJson();
                String status = resp.getString("Status");
                if (status.equals("liked")) {
                    System.out.println("liked");
                }

                break;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void addToPlaylist() {
        //switch to adding playlist scene
    }

}

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
    @FXML
    private ImageView coverImage;
    private int trackID;

    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    private ProgressBar songProgressBar;
    private Timer timer;
    private TimerTask task;
    private boolean running;

    private Parent root;

    public int getTrackID() {
        return trackID;
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
    }

    public void info(String path, String title, String artist, String album, String genre, double popularity, String releaseDate, int ID){
        songLabel.setText(title);
        artistLabel.setText(artist);
        albumLabel.setText(album);
        genreLabel.setText(genre);
        popularityLabel.setText(String.valueOf(popularity));
        releaseDateLabel.setText(releaseDate);
        setTrackID(ID);
//        System.out.println(ID);

        try {
            Socket socket = MainController.socket;
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

                if (response.getJson().getString("Status").equals("Find song path")){
                    receiveFile(response.getJson(),title,artist);

                    //String coverPath = receiveImage(response.getJson().getString("Cover"));
                    Image cover = new Image("C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\"+ trackID +".jpg");
                    coverImage.setImage(cover);

                    media = new Media(new File(path).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    break;
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void play() {
        beginTimer();
        mediaPlayer.play();
    }
    public void pause() {
        cancelTimer();
        mediaPlayer.pause();
    }

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);

                if (current/end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task,1000,1000);
    }
    public void cancelTimer() {
        running = false;
        timer.cancel();
    }

    public void like() {
        try {
            Socket socket = MainController.socket;
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
    public void addToPlaylist() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/add-song-to-playlist.fxml"));
        root = loader.load();

        Stage newStage = new Stage();
        Image icon = new Image("C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\spotifyIcon.png");
        newStage.getIcons().add(icon);

        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    private static void receiveFile(JSONObject jsonObject,String title, String artist) throws Exception {
        System.out.println("here");
        String encodedData = jsonObject.getString("musicData");
        byte[] fileData = Base64.getDecoder().decode(encodedData);
        String filePath = "C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\"+ title + artist +".mp3";//TODO
        Files.write(Paths.get(filePath), fileData);
        //player.play(filePath);
    }

    public static String receiveImage(String cover,String title,String artist) throws IOException {
        String encodedImageData = cover;
        byte[] fileData = Base64.getDecoder().decode(encodedImageData);
        String filePath = "C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\"+ title + artist +".jpg";
        Files.write(Paths.get(filePath), fileData);
        return (filePath);
//        System.out.println("Image file is saved at: " + ;
    }

}

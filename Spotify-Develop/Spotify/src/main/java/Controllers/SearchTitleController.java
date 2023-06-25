package Controllers;

import Classes.Music;
import Shared.Request;
import Shared.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class SearchTitleController {
    @FXML
    private TextField tfSongTitle;
    @FXML
    private ListView<Music> songsList;

    private ObservableList<Music> songList;

    private Parent root;

    public void exit(ActionEvent event) throws IOException{
        Controller.changeScene(event, "/search-page.fxml");
    }

    public void switchToResult(ActionEvent event) {

        try {
            Socket socket = MainController.socket;
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(output, true);
            Request request = new Request();
            Response response = new Response();

            JSONObject json;
            String title=tfSongTitle.getText();
            json=new JSONObject();
            json.put("Command","Search title");
            json.put("Title",title);
            request.setJson(json);//create request

            out.println(request.getJson().toString());
            response.setJson(new JSONObject(in.readLine()));

            while (response.getJson() != null) {

                JSONObject resp=response.getJson();
                String status = resp.getString("Status");

                if (status.equals("Searching title")) {

                    response.setJson(new JSONObject(in.readLine()));

                    songList = FXCollections.observableArrayList();

                    while (response.getJson().has("Music")) {
                        Music music = new Music(response.getJson().getInt("TrackID"),response.getJson().getString("Title"),response.getJson().getString("Artist"),
                                response.getJson().getString("Album"),response.getJson().getString("Genre"),response.getJson().getString("Duration"),
                                response.getJson().getString("ReleaseDate"),response.getJson().getDouble("Popularity"),response.getJson().getString("MusicPath"));
                        songsList.setCellFactory(param -> new ListCell<Music>() {
                            @Override
                            protected void updateItem(Music item, boolean empty) {
                                super.updateItem(item,empty);
                                if (empty || item == null) {
                                    setText(null);
                                } else {
                                    setText(item.getTitle() + "\n" + item.getArtist());
                                }
                            }
                        });

                        songList.add(music);

                        response.setJson(new JSONObject(in.readLine()));//receive response from server
                    }
                    songsList.setItems(songList);

                    songsList.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent click) {

                            if (click.getClickCount() == 1) {

                                String path = songsList.getSelectionModel().getSelectedItem().getMusicPath();
                                String songTitle = songsList.getSelectionModel().getSelectedItem().getTitle();
                                String artist = songsList.getSelectionModel().getSelectedItem().getArtist();
                                String album = songsList.getSelectionModel().getSelectedItem().getAlbum();
                                String genre = songsList.getSelectionModel().getSelectedItem().getGenre();
                                double popularity = songsList.getSelectionModel().getSelectedItem().getPopularity();
                                String releaseDate = songsList.getSelectionModel().getSelectedItem().getReleaseDate();
                                int ID = songsList.getSelectionModel().getSelectedItem().getTrackID();

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/search-song-result.fxml"));
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                SearchSongResultController controller = loader.getController();
                                controller.info(path,songTitle,artist,album,genre,popularity,releaseDate,ID);

                                Stage newStage = new Stage();
                                Image icon = new Image("C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\spotifyIcon.png");
                                newStage.getIcons().add(icon);

                                Scene scene = new Scene(root);
                                newStage.setScene(scene);
                                newStage.show();

                            }
                        }
                    });

                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}

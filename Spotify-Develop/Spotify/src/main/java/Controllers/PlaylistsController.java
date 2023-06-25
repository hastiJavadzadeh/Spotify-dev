package Controllers;

import Classes.Music;
import Classes.Playlist;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class PlaylistsController {

    @FXML
    private ListView<Playlist> playlistL;
    private ObservableList<Music> playlistSongs;

    private Parent root;
    private Stage stage;
    private Scene scene;

    public void showPlaylists(ObservableList<Playlist> playlists){
        playlistL.setCellFactory(param -> new ListCell<Playlist>() {
            @Override
            protected void updateItem(Playlist item, boolean empty) {
                super.updateItem(item,empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPlName());
                }
            }
        });

        playlistL.setItems(playlists);

        playlistL.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 1) {

                    try {
                        Socket socket = MainController.socket;
                        InputStream input = socket.getInputStream();
                        OutputStream output = socket.getOutputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(input));
                        PrintWriter out = new PrintWriter(output, true);
                        Request request = new Request();
                        Response response = new Response();

                        JSONObject json = new JSONObject();

                        json.put("Command","Show playlist songs");
                        json.put("Name",playlistL.getSelectionModel().getSelectedItem().getPlName());
                        request.setJson(json);

                        out.println(request.getJson().toString());
                        response.setJson(new JSONObject(in.readLine()));
                        System.out.println(response.getJson());
                        while (response.getJson() != null) {

                            JSONObject resp=response.getJson();
                            String status = resp.getString("Status");

                            System.out.println(response.getJson().getString("Music"));
                            if (status.equals("Show playlist")) {

                                response.setJson(new JSONObject(in.readLine()));
                                playlistSongs = FXCollections.observableArrayList();
                                while (response.getJson().has("Music")) {

                                    System.out.println(response.getJson().getString("Music"));
//                                    Music music = new Music(response.getJson().getInt("TrackID"),response.getJson().getString("Title"),response.getJson().getString("Artist"),
//                                            response.getJson().getString("Album"),response.getJson().getString("Genre"),response.getJson().getString("Duration"),
//                                            response.getJson().getString("ReleaseDate"),response.getJson().getDouble("Popularity"),response.getJson().getString("MusicPath"));
//
//                                    playlistSongs.add(music);

                                    response.setJson(new JSONObject(in.readLine()));
                                }
//                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/songs-of-playlist.fxml"));
//                                root = loader.load();
//
//                                SongsOfPlaylistController controller = loader.getController();
//                                controller.showMusics(playlistSongs);
//
//                                stage = (Stage)((Node)click.getSource()).getScene().getWindow();
//                                scene = new Scene(root);
//                                stage.setScene(scene);
//                                stage.show();

                                break;
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


//                    String path = playlistL.getSelectionModel().getSelectedItem().getMusicPath();
//                    String songTitle = playlistL.getSelectionModel().getSelectedItem().getTitle();
//                    String artist = playlistL.getSelectionModel().getSelectedItem().getArtist();
//                    String album = playlistL.getSelectionModel().getSelectedItem().getAlbum();
//                    String genre = playlistL.getSelectionModel().getSelectedItem().getGenre();
//                    double popularity = playlistL.getSelectionModel().getSelectedItem().getPopularity();
//                    String releaseDate = musicListView.getSelectionModel().getSelectedItem().getReleaseDate();
//                    int ID = musicListView.getSelectionModel().getSelectedItem().getTrackID();
//
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/search-song-result.fxml"));
//                    try {
//                        root = loader.load();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    SearchSongResultController controller = loader.getController();
//                    controller.info(path,songTitle,artist,album,genre,popularity,releaseDate,ID);
//
//                    Stage newStage = new Stage();
//                    Image icon = new Image("C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\spotifyIcon.png");
//                    newStage.getIcons().add(icon);
//
//                    Scene scene = new Scene(root);
//                    newStage.setScene(scene);
//                    newStage.show();
                }
            }
        });
    }
    public void exit(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/welcome-page.fxml");
    }
}

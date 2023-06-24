package Controllers;

import Classes.Music;
import Classes.Playlist;
import Shared.Request;
import Shared.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;

public class LibraryController {
    @FXML
    private ListView<Music> musicListView;

    private ObservableList<Playlist> playlists;

    private Parent root;
    private Stage stage;
    private Scene scene;

    public void switchToWelcome(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/welcome-page.fxml");
    }
    public void switchToLikedSongs(ActionEvent event) throws IOException {
        //Controller.changeScene(event, "liked-songs.fxml");
    }
    public void switchToNewPlaylist(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/add-new-playlist.fxml");
    }
    public void switchToPLaylists(ActionEvent event) throws IOException {

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
            json.put("Command","View playlists");
            request.setJson(json);

            out.println(request.getJson().toString());
            response.setJson(new JSONObject(in.readLine()));

            while (response.getJson() != null) {

                JSONObject resp=response.getJson();
                String status = resp.getString("Status");

                if (status.equals("Display playlists")){

                    response.setJson(new JSONObject(in.readLine()));
                    playlists = FXCollections.observableArrayList();
                    while (response.getJson().has("Name")) {
                        System.out.println(response.getJson().getString("Name"));

                        Playlist playlist = new Playlist(response.getJson().getInt("userID"),response.getJson().getInt("playlistID"),response.getJson().getString("Name"));
                        playlists.add(playlist);

                        response.setJson(new JSONObject(in.readLine()));
                    }
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/playlists.fxml"));
                    root = loader.load();

                    PlaylistsController controller = loader.getController();
                    controller.showPlaylists(playlists);

                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    break;
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }


    public void showMusics(ObservableList<Music> songList){

        musicListView.setCellFactory(param -> new ListCell<Music>() {
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

        musicListView.setItems(songList);

        musicListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 1) {

                    String path = musicListView.getSelectionModel().getSelectedItem().getMusicPath();
                    String songTitle = musicListView.getSelectionModel().getSelectedItem().getTitle();
                    String artist = musicListView.getSelectionModel().getSelectedItem().getArtist();
                    String album = musicListView.getSelectionModel().getSelectedItem().getAlbum();
                    String genre = musicListView.getSelectionModel().getSelectedItem().getGenre();
                    double popularity = musicListView.getSelectionModel().getSelectedItem().getPopularity();
                    String releaseDate = musicListView.getSelectionModel().getSelectedItem().getReleaseDate();
                    int ID = musicListView.getSelectionModel().getSelectedItem().getTrackID();

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
    }
}


package Controllers;

import Classes.Music;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class LibraryController {
    @FXML
    private ListView<Music> musicListView;

    private Parent root;
    private Stage stage;
    private Scene scene;

    public void switchToWelcome(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/welcome-page.fxml");
    }
    public void switchToLikedSongs(ActionEvent event) throws IOException {
        //Controller.changeScene(event, "liked-songs.fxml");
    }


    public void showMusics(ObservableList<Music> songList,ActionEvent event){

        ActionEvent actionEvent = new ActionEvent();
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

        musicListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal != null) {

                String path = musicListView.getSelectionModel().getSelectedItem().getMusicPath();
                String songTitle = musicListView.getSelectionModel().getSelectedItem().getTitle();
                String artist = musicListView.getSelectionModel().getSelectedItem().getArtist();
                String album = musicListView.getSelectionModel().getSelectedItem().getAlbum();
                String genre = musicListView.getSelectionModel().getSelectedItem().getGenre();
                double popularity = musicListView.getSelectionModel().getSelectedItem().getPopularity();
                String releaseDate = musicListView.getSelectionModel().getSelectedItem().getReleaseDate();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/search-song-result.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                SearchSongResultController controller = loader.getController();

                controller.info(path,songTitle,artist,album,genre,popularity,releaseDate);

                //System.out.println(actionEvent.getSource());

                stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }

        });
    }
}
//EventHandler<MouseEvent>() {
//
//@Override
//public void handle(MouseEvent event) {
//        System.out.println("clicked on " + lv.getSelectionModel().getSelectedItem());
//        }

package Controllers;

import Classes.Music;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class SongsOfPlaylistController {
    private Parent root;

    @FXML
    private Label plName;
    @FXML
    private ListView<Music> songsL;

    public void showMusics(ObservableList<Music> songList){

        songsL.setCellFactory(param -> new ListCell<Music>() {
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

        songsL.setItems(songList);

        songsL.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 1) {

                    String path = songsL.getSelectionModel().getSelectedItem().getMusicPath();
                    String songTitle = songsL.getSelectionModel().getSelectedItem().getTitle();
                    String artist = songsL.getSelectionModel().getSelectedItem().getArtist();
                    String album = songsL.getSelectionModel().getSelectedItem().getAlbum();
                    String genre = songsL.getSelectionModel().getSelectedItem().getGenre();
                    double popularity = songsL.getSelectionModel().getSelectedItem().getPopularity();
                    String releaseDate = songsL.getSelectionModel().getSelectedItem().getReleaseDate();
                    int ID = songsL.getSelectionModel().getSelectedItem().getTrackID();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/search-song-result.fxml"));
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    SearchSongResultController controller = loader.getController();
                    controller.info(path,songTitle,artist,album,genre,popularity,releaseDate,ID);

                    Stage newStage = new Stage();
                    javafx.scene.image.Image icon = new Image("C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\spotifyIcon.png");
                    newStage.getIcons().add(icon);

                    Scene scene = new Scene(root);
                    newStage.setScene(scene);
                    newStage.show();
                }
            }
        });
    }

    public void exit(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/welcome-page.fxml");
    }
}

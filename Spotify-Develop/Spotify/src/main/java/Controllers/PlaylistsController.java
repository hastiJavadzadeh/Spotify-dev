package Controllers;

import Classes.Music;
import Classes.Playlist;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class PlaylistsController {

    @FXML
    private ListView<Playlist> playlistL;

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
    }
    public void exit(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/welcome-page.fxml");
    }
}

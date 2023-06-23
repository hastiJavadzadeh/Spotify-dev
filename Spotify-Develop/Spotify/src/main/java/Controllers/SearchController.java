package Controllers;

import javafx.event.ActionEvent;
import java.io.*;

public class SearchController {

    public void switchToWelcome(ActionEvent event) throws IOException{
        Controller.changeScene(event, "/welcome-page.fxml");
    }
    public void switchToArtistSearch(ActionEvent event) throws IOException{
        Controller.changeScene(event, "/search-artist.fxml");
    }

    public void switchToAlbumSearch(ActionEvent event) throws IOException{
        Controller.changeScene(event, "/search-album.fxml");
    }

    public void switchToGenreSearch(ActionEvent event) throws IOException{
        Controller.changeScene(event, "/search-genre.fxml");
    }
    public void switchToSongSearch(ActionEvent event) throws IOException{
        Controller.changeScene(event, "/search-title.fxml");
    }

}

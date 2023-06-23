package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Controller {
    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    public static void changeScene(ActionEvent event, String fxmlFile) throws IOException {

        FXMLLoader loader = new FXMLLoader(Controller.class.getResource(fxmlFile));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

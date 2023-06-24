package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class SettingsController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Label userIDLabel;
    @FXML
    private Label playListIDLabel;
    @FXML
    private ImageView profilePic;

    public void switchToWelcome(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/welcome-page.fxml");
    }
    public void displayInfo(String username,String email,String pass,String birthday,String userID,String playListID,String imagePath){
        usernameLabel.setText(username);
        emailLabel.setText(email);
        passwordLabel.setText(pass);
        birthdayLabel.setText(birthday);
        userIDLabel.setText(userID);
        playListIDLabel.setText(playListID);

        Image profile = new Image(imagePath);
        profilePic.setImage(profile);
    }
}

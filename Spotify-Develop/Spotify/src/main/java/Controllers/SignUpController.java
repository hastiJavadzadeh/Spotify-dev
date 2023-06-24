package Controllers;

import Shared.Request;
import Shared.Response;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController {

    static JSONObject user=new JSONObject();

    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private Button buttonSignup;

    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfBirthday;
    @FXML
    private TextField tfPicture;

    public void switchToLogin(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/logged-in.fxml");
    }
    public void switchToWelcome(ActionEvent event) throws IOException {
        if (!tfUsername.getText().trim().isEmpty() && !tfPassword.getText().trim().isEmpty() && !tfEmail.getText().trim().isEmpty() && !tfBirthday.getText().trim().isEmpty() && !tfPicture.getText().trim().isEmpty()){

            String username = tfUsername.getText();
            String password = tfPassword.getText();
            String email = tfEmail.getText();
            String birthday = tfBirthday.getText();
            String profilePath = tfPicture.getText();

            try {
                Socket socket = new Socket("127.0.0.1", 2345);
                System.out.println("Connected to server!");
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                PrintWriter out = new PrintWriter(output, true);
                Request request = new Request();
                Response response = new Response();

                JSONObject json = new JSONObject();

                json.put("Command","SignUp");
                json.put("username", username);
                json.put("password", password);
                json.put("Birthday",birthday);
                json.put("Email",email);
                json.put("ImagePath",profilePath);
                request.setJson(json);

                out.println(request.getJson().toString());
                response.setJson(new JSONObject(in.readLine()));

                while (response.getJson() != null) {

                    JSONObject resp=response.getJson();
                    String status = resp.getString("Status");

                    if (status.equals("Successfully signup")) {

                        Controller.changeScene(event, "/welcome-page.fxml");
                        //user.put("username", resp.getString("username"));
                        break;

                    } else if (status.equals("Fail signup")) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("This username already exists");
                        alert.show();
                        break;

                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all information");
            alert.show();
        }
    }
}


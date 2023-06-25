package Controllers;

import Server.ServerMain;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoggedInController  {

    static JSONObject user=new JSONObject();
    @FXML
    private Button buttonLogin;
    @FXML
    private Button buttonSignup;
    @FXML
    private  TextField tfUsername;
    @FXML
    private  TextField tfPassword;

    public void logIn(ActionEvent event) throws IOException, SQLException {

        if (!tfUsername.getText().trim().isEmpty() && !tfPassword.getText().trim().isEmpty()){

            String username = tfUsername.getText();
            String password = tfPassword.getText();

            try {
                Socket socket = MainController.socket;

                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                PrintWriter out = new PrintWriter(output, true);
                Request request = new Request();
                Response response = new Response();

                JSONObject json = new JSONObject();

                json.put("Command","Login");
                json.put("username",username);
                json.put("password",password);
                request.setJson(json);

                out.println(request.getJson().toString());
                response.setJson(new JSONObject(in.readLine()));

                while (response.getJson() != null) {

                    JSONObject resp=response.getJson();
                    String status = resp.getString("Status");
                    if (status.equals("Successfully login")) {
                        //System.out.println(resp.getString("id"));
                        //user.put("username", resp.getString("username"));
                        Controller.changeScene(event, "/welcome-page.fxml");
                        break;
                    } else if (status.equals("Fail login")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("User does not exist");
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

    public void switchToSignup(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/signUp.fxml");
    }

}

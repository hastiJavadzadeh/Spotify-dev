package Controllers;

import Shared.Request;
import Shared.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class AddNewPlaylistController {
    @FXML
    private TextField nameTF;

    public void add(ActionEvent event) {
        if (!nameTF.getText().trim().isEmpty()) {
            try {
                Socket socket = MainController.socket;
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                PrintWriter out = new PrintWriter(output, true);
                Request request = new Request();
                Response response = new Response();

                JSONObject json = new JSONObject();

                json.put("Command","Create playlist");
                json.put("Name",nameTF.getText());
                request.setJson(json);

                out.println(request.getJson().toString());
                response.setJson(new JSONObject(in.readLine()));

                while (response.getJson() != null) {

                    JSONObject resp=response.getJson();
                    String status = resp.getString("Status");

                    if (status.equals("created playlist")) {

                        System.out.println("done");//todo
                        Controller.changeScene(event,"/welcome-page.fxml");
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
            alert.setContentText("You should pick a name first!");
            alert.show();
        }


    }
}

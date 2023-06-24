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

public class AddSongToPlaylistController {
    @FXML
    private TextField nameTF;

    public void add(ActionEvent event) {
        if (!nameTF.getText().trim().isEmpty()) {
            try {
                Socket socket = new Socket("127.0.0.1", 2345);
                System.out.println("Connected to server!");
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                PrintWriter out = new PrintWriter(output, true);
                Request request = new Request();
                Response response = new Response();

                SearchSongResultController controller = new SearchSongResultController();
                int id = controller.getTrackID();

                JSONObject json = new JSONObject();

                json.put("Command","toPlaylist");
                json.put("trackID",id);
                json.put("playlist",nameTF.getText());
                request.setJson(json);

                out.println(request.getJson().toString());
                response.setJson(new JSONObject(in.readLine()));

                while (response.getJson() != null) {
                    JSONObject resp=response.getJson();
                    String status = resp.getString("Status");
                    if (status.equals("added to playlist")) {
                        System.out.println("added");
                    }

                    break;
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
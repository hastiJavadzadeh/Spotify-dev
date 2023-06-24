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
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.URL;

import static Controllers.LoggedInController.user;

public class WelcomePageController {
    private Parent root;
    private Stage stage;
    private Scene scene;


    //static JSONObject user=new JSONObject();
    @FXML
    private Button buttonLogout;
    @FXML
    private Button buttonSearch;
    @FXML
    private Label labelWelcome;

    private ObservableList<Music> songList;


    public void switchToLogin(ActionEvent event) throws IOException {//LOG OUT
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

            json.put("Command","Logout");
            request.setJson(json);

            out.println(request.getJson().toString());
            response.setJson(new JSONObject(in.readLine()));

            while (response.getJson() != null) {

                JSONObject resp=response.getJson();
                String status = resp.getString("Status");
                if (status.equals("Logged out")) {
                    System.out.println("logged out");
                    Controller.changeScene(event, "/logged-in.fxml");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //Controller.changeScene(event, "/logged-in.fxml");
    }

    public void switchToSearch(ActionEvent event) throws IOException {
        Controller.changeScene(event,"/search-page.fxml");
    }

    public void switchToLibrary(ActionEvent event) throws IOException {

        try {
            Socket socket = new Socket("127.0.0.1", 2345);
            System.out.println("Connected to server!");
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(output, true);
            Request request = new Request();
            Response response = new Response();

            JSONObject json=new JSONObject();
            json.put("Command","Music library");
            request.setJson(json);

            out.println(request.getJson().toString());
            response.setJson(new JSONObject(in.readLine()));

            while (response.getJson() != null) {

                JSONObject resp=response.getJson();
                String status = resp.getString("Status");

                if (status.equals("Display songs")){

                    response.setJson(new JSONObject(in.readLine()));
                    songList = FXCollections.observableArrayList();
                    while (response.getJson().has("MusicData")) {

                        Music music = new Music(response.getJson().getInt("TrackID"),response.getJson().getString("Title"),response.getJson().getString("Artist"),
                                response.getJson().getString("Album"),response.getJson().getString("Genre"),response.getJson().getString("Duration"),
                                response.getJson().getString("ReleaseDate"),response.getJson().getDouble("Popularity"),response.getJson().getString("MusicPath"));

                        songList.add(music);

                        response.setJson(new JSONObject(in.readLine()));
                    }
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library.fxml"));
                    root = loader.load();

                    LibraryController controller = loader.getController();
                    controller.showMusics(songList);

                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    break;
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public void switchToSettings(ActionEvent event) {

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
            json.put("Command","View profile");

            request.setJson(json);

            out.println(request.getJson().toString());
            response.setJson(new JSONObject(in.readLine()));

            while (response.getJson() != null) {

                JSONObject resp = response.getJson();
                String status = resp.getString("Status");
                if (status.equals("View profile")) {

                    response.setJson(new JSONObject(in.readLine()));//receive response from server
                    while (response.getJson().has("user")) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings.fxml"));
                        root = loader.load();

                        SettingsController settingsController = loader.getController();

                        settingsController.displayInfo(response.getJson().getString("username"),response.getJson().getString("email"),response.getJson().getString("password"),
                                response.getJson().getString("birthday"),response.getJson().getString("UserID"),response.getJson().getString("playlistID"));

                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                        response.setJson(new JSONObject(in.readLine()));//receive response from server
                    }

                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}

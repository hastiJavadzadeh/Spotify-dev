package Server;

import Shared.Request;
import Shared.Response;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static Server.ServerMain.clients;
import static Server.ServerMain.handle;

class ClientHandler extends Thread{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static int ID;//TODO
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    public void run() {
        Response response;
        try{
            Request request = new Request();
            request.setJson(new JSONObject( in.readLine()));//receive request from client
            while(request.getJson()!=null){
                response = handle(request,out,ID);//create new response
                if(response.getJson()!=null) {
                    if((response.getJson().getString("Status").equals("Successfully login")) || (response.getJson().getString("Status").equals("Successfully signup"))){
                        ID=response.getJson().getInt("id");
                    }
                    if(response.getJson().getString("Status").equals("Logged out")){
                        out.println(response.getJson().toString());//send response to client
                        logout();
                    }
                    out.println(response.getJson().toString());//send response to client
                }
                request.setJson(new JSONObject( in.readLine()));//receive request from client
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
                clients.remove(this);
                System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void logout() throws IOException {
        socket.close();
        clients.remove(this);
        ServerMain.Client.remove(ID);
    }
}
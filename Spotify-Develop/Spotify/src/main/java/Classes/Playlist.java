package Classes;

public class Playlist {
    private int userID;
    private int playlistID;
    private String plName;

    public Playlist(int userID, int playlistID, String plName) {
        this.userID = userID;
        this.playlistID = playlistID;
        this.plName = plName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public String getPlName() {
        return plName;
    }

    public void setPlName(String plName) {
        this.plName = plName;
    }
}

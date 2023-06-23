package Database;

import Classes.Music;

import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ImportData {
    public ImportData() throws SQLException, URISyntaxException {
        ArrayList<Music>musics=new ArrayList<>();
        //TODO create a variable containing absolute path and use it
        Music SoldierSide=new Music(UUID.randomUUID().toString().hashCode(),"Soldier side","System of a down","Mezmerize","hard rock","3,39","2005",8.9,"C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\SoldierSide.mp3");
        Music BornToTouchYourFeelings=new Music(UUID.randomUUID().toString().hashCode(),"Born To Touch Your Feelings","Scorpions","","hard rock","4,02","2017",9.1,"C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\born_to_touch_your_feelings.mp3");
        Music ILoveYouBillie=new Music(UUID.randomUUID().toString().hashCode(),"I love you","Billie Eilish","","Pop","4,51","2019",9.1,"C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\Billie_Eilish_I_Love_You_128.mp3");
        Music ILoveYouRF=new Music(UUID.randomUUID().toString().hashCode(),"I love you","Rauf & Faik","Запомни I love you","Pop","2,50","2019",9.1,"C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\rauf_faik_zapomni_i_love_you 128.mp3");
        Music That_sNotHowThisWorks=new Music(UUID.randomUUID().toString().hashCode(), "That_s Not How This Works","Charlie Puth","","Pop","2,45","2023",9.1,"C:\\Users\\astan\\Spotify-dev\\Spotify-Develop\\Spotify\\src\\main\\resources\\Charlie Puth - That_s Not How This Works (feat. Dan Shay).mp3");

        musics.add(SoldierSide);
        musics.add(BornToTouchYourFeelings);
        musics.add(ILoveYouBillie);
        musics.add(ILoveYouRF);
        musics.add(That_sNotHowThisWorks);

        for(Music music : musics){
            int ID=music.getTrackID();
            String title = music.getTitle();
            String artist = music.getArtist();
            ResultSet resultSet=DataBase.query("SELECT * FROM \"Spotify\".\"Music\" WHERE \"Title\" = " + "'" + title + "'" + "AND" + "\"Artist\" = " + "'" + artist + "'");
            if(!resultSet.next()){
                String sql = "INSERT INTO\"Spotify\".\"Music\" VALUES ('"  + music.getTitle() + "', '" +
                        music.getArtist() + "', '" + music.getAlbum() + "','" + music.getGenre() + "', '" + music.getDuration() + "', '" +
                        music.getReleaseDate() + "', '" + music.getPopularity() + "','" + music.getMusicPath() +"', '"+ music.getTrackID() + "')";
                DataBase.query(sql);
            }
        }
    }

}

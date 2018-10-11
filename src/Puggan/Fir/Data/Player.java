package Puggan.Fir.Data;

import Puggan.Fir.Api;
import javax.json.JsonObject;
import java.io.IOException;

public class Player
{
    public int Player_ID;
    public String User_Name;

    public Player(JsonObject json)
    {
        Player_ID = json.getInt("Player_ID");
        User_Name = json.getString("User_Name");
    }

    public static Player load(int Player_ID)
    {
        try
        {
            return new Player(Api.object("player/" + Player_ID));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

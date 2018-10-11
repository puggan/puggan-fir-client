package Puggan.Fir.Data;

import Puggan.Fir.Api;

import javax.json.JsonObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Token
{
    public String Token;
    public int Player_ID;
    public Player player;

    public Token(JsonObject json)
    {
        Token = json.getString("Token");
        Player_ID = json.getInt("Player_ID");
        player = new Player(json.getJsonObject("Player"));
    }

    // Debug
    public Token(int p, String t)
    {
        Token = t;
        Player_ID = p;
        player = Player.load(p);
    }

    public static Token auth(String User_Name) throws IOException
    {
        return token_api_player("player/auth", User_Name);
    }

    public static Token register(String User_Name) throws IOException
    {
        return token_api_player("player/add", User_Name);
    }

    public static Token token_api_player(String api, String User_Name) throws IOException
    {
        String data = "username=" + URLEncoder.encode(User_Name, StandardCharsets.UTF_8);
        JsonObject json = Api.object(api, data);
        return new Token(json);
    }
}

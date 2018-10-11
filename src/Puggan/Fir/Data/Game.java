package Puggan.Fir.Data;

import Puggan.Fir.Api;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Game
{
    public int Game_ID;
    public int Player1_ID;
    public int Player2_ID;
    public String Status;
    public String Start_Time;
    public String Player1;
    public String Player2;

    public static final String WAIT1 = "waiting for player 1";
    public static final String WAIT2 = "waiting for player 2";
    public static final String WON1 = "won by player 1";
    public static final String WON2 = "won by player 2";
    public static final String TIE = "tie";


    public Game(@NotNull JsonObject json)
    {
        Game_ID = json.getInt("Game_ID");
        Player1_ID = json.getInt("Player1_ID");
        Player2_ID = json.getInt("Player2_ID");
        Status = json.getString("Status");
        Start_Time = json.getString("Start_Time");
        Player1 = json.getString("Player1");
        Player2 = json.getString("Player2");
    }

    public boolean playable(int player_id)
    {
        if (Player1_ID == player_id)
        {
            if (Status.equals(WAIT1))
            {
                return true;
            }
        }
        if (Player2_ID == player_id)
        {
            if (Status.equals(WAIT2))
            {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Contract("_ -> new")
    public static Game load(int Game_ID) throws IOException
    {
        return new Game(Api.object("game/" + Game_ID));
    }

    @NotNull
    @Contract("_, _ -> new")
    public static Game challenge(String token, String username) throws IOException
    {
        StringBuilder data = new StringBuilder();
        data.append("token=");
        data.append(URLEncoder.encode(token, StandardCharsets.UTF_8));
        data.append("&opponent=");
        data.append(URLEncoder.encode(username, StandardCharsets.UTF_8));
        return new Game(Api.object("game/add", data.toString()));
    }

    public Game play(String token, int x, int y) throws IOException
    {
        String data = "token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        return new Game(Api.object("play/" + Game_ID + '/' + x + '/' + y, data));
    }

    public static List<Game> player_games(int player_id) throws IOException
    {
        List<Game> games = new ArrayList<>();
        JsonArray json_games = Api.array("games/" + player_id);
        for(JsonObject json : json_games.getValuesAs(JsonObject.class))
        {
            games.add(new Game(json));
        }
        return games;
    }
}

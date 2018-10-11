package Puggan.Fir.Data;

import Puggan.Fir.Api;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pawn
{
    public int Game_ID;
    public int X;
    public int Y;
    public String Color;
    public int Nr;
    public int Side;

    public Pawn(JsonObject json)
    {
        Game_ID = json.getInt("Game_ID");
        X = json.getInt("X");
        Y = json.getInt("Y");
        Nr = json.getInt("NR");
        setColor(json.getString("Color"));
    }

    public int setColor(String color)
    {
        Color = color;
        if (Color.equals("Red"))
        {
            Side = 1;
            return Side;
        }
        if (Color.equals("Blue"))
        {
            Side = 2;
            return Side;
        }
        Side = 0;
        return Side;
    }

    public static List<Pawn> load_game(Game game) throws IOException
    {
        return load_game(game.Game_ID);
    }

    public static List<Pawn> load_game(int game_id) throws IOException
    {
        List<Pawn> list = new ArrayList<>();

        JsonArray json_list = Api.array("game/" + game_id + "/pawns");
        int length = json_list.size();
        for (JsonObject json : json_list.getValuesAs(JsonObject.class))
        {
            list.add(new Pawn(json));
        }
/*
        for (int position = 0; position < length; position++)
        {
            list.add(new Pawn(json_list.getJsonObject(position)));
        }
*/
        return list;
    }
}

package Puggan.Fir;

import Puggan.Fir.Exceptions.ApiException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Api
{
    static InputStream ajax;

    // Get
    public static JsonReader reader(String url_part) throws IOException
    {
        URL url = new URL(Strings.api_url() + url_part);
        ajax = url.openStream();
        ajax = new SplitInput(ajax);
        return Json.createReader(ajax);
    }

    public static JsonObject object(String url_part) throws IOException
    {
        return reader(url_part).readObject();
    }

    public static JsonArray array(String url_part) throws IOException
    {
        return reader(url_part).readArray();
    }

    // Post
    public static JsonReader reader(String url_part, String data) throws IOException
    {
        URL url = new URL(Strings.api_url() + url_part);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        DataOutputStream data_stream = new DataOutputStream(connection.getOutputStream());
        data_stream.writeBytes(data);
        data_stream.flush();
        data_stream.close();

        int http_code = connection.getResponseCode();
        if (http_code != 200)
        {
            ajax = connection.getErrorStream();
            ajax = new SplitInput(ajax);
            JsonReader error_reader = Json.createReader(ajax);
            JsonObject error_json = error_reader.readObject();
            System.err.print(error_json.toString());
            String error = error_json.getString("message");
            if (error.length() > 0)
            {
                throw new ApiException(error);
            }
            throw new ApiException("Bad http code: " + http_code);
        }

        ajax = connection.getInputStream();
        ajax = new SplitInput(ajax);
        return Json.createReader(ajax);
    }

    public static JsonObject object(String url_part, String data) throws IOException
    {
        // Debug
        JsonReader reader = reader(url_part, data);
        try
        {
            JsonObject o = reader.readObject();
            System.err.print(o.toString());
            print_debug();
            return o;
        }
        catch (JsonParsingException e)
        {
            print_debug();
            throw e;
        }

        // Not debug:
        //return reader(url_part, data).readObject();
    }

    public static JsonArray array(String url_part, String data) throws IOException
    {
        return reader(url_part, data).readArray();
    }

    public static void print_debug()
    {
        if (ajax instanceof SplitInput)
        {
            System.err.print("\n -- Ajax --\n");
            System.err.print(ajax.toString());
            System.err.print("\n -- / Ajax --\n");
        }
    }
}

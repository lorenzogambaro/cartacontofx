/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public final class ApiConnection 
{
    private ApiConnection() {}
    
    public static JsonObject getDatas(final String urlString) throws IOException 
    {
        final URL url = new URL(urlString);
        
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        con.setRequestMethod("GET");

        final StringBuilder response = new StringBuilder();
        try (final Scanner sc = new Scanner(con.getInputStream())) {
            while (sc.hasNextLine())
                response.append(sc.nextLine());
        }

        con.disconnect();
        
        return JsonParser.parseString(response.toString()).getAsJsonObject();
    }
    public static JsonObject sendData(final String urlString, final JsonObject jsonObject) throws IOException 
    {
        final URL url = new URL(urlString);

        final HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (final OutputStream os = con.getOutputStream()) 
        {
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        final StringBuilder response = new StringBuilder();
        try (final Scanner sc = new Scanner(con.getInputStream()))
        {
            while (sc.hasNextLine())
                response.append(sc.nextLine());
        }
        
        con.disconnect();

        return JsonParser.parseString(response.toString()).getAsJsonObject();
    }
}

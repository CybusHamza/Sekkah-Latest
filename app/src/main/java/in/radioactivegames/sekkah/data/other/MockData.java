package in.radioactivegames.sekkah.data.other;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Scanner;

/**
 * Created by AntiSaby on 12/2/2017.
 * www.radioactivegames.in
 */

public class MockData
{
    private static Gson gson = new Gson();
    public static JSONArray stations, trains, nestedStations;

    static
    {
        try
        {
            stations = new JSONArray();
            stations.put(new JSONObject()
                .put("id", 1)
                .put("name", "Jalandhar"))
            .put(new JSONObject()
                .put("id", 2)
                .put("name", "Ludhiana"));
/*            .put(new JSONObject()
                .put("id", 3)
                .put("name", "Amritsar"));*/

            trains = new JSONArray();
            trains.put(new JSONObject()
                .put("id", 1)
                .put("trainNumber", 1001)
                .put("classEn", "VIP")
                .put("classAr", "VIP")
                .put("departureTime", "6:00 AM")
                .put("destinationTime", "8:00 AM")
                .put("departureStation", "Jalandhar")
                .put("destinationStation", "Amritsar")
            )
            .put(new JSONObject()
                .put("id", 2)
                .put("trainNumber", 1002)
                .put("classEn", "VIP")
                .put("classAr", "VIP")
                .put("departureTime", "10:30 PM")
                .put("destinationTime", "11:00 PM")
                .put("departureStation", "Amritsar")
                .put("destinationStation", "Ludhiana")
            )
            .put(new JSONObject()
                    .put("id", 3)
                    .put("trainNumber", 1003)
                    .put("classEn", "VIP")
                    .put("classAr", "VIP")
                    .put("departureTime", "07:47 PM")
                    .put("destinationTime", "10:00 PM")
                    .put("departureStation", "Jalandhar")
                    .put("destinationStation", "Ludhiana")
            );


            nestedStations = new JSONArray();
            nestedStations.put(new JSONObject()
                    .put("id", 1)
                    .put("name", "Aswan")
                    .put("arrivalTime", "10:00 AM")
                    .put("departureTime", "11:00 AM"));
            nestedStations.put(new JSONObject()
                    .put("id", 1)
                    .put("name", "Aswan")
                    .put("arrivalTime", "10:00 AM")
                    .put("departureTime", "11:00 AM"));
            nestedStations.put(new JSONObject()
                    .put("id", 1)
                    .put("name", "Aswan")
                    .put("arrivalTime", "10:00 AM")
                    .put("departureTime", "11:00 AM"));
            nestedStations.put(new JSONObject()
                    .put("id", 1)
                    .put("name", "Aswan")
                    .put("arrivalTime", "10:00 AM")
                    .put("departureTime", "11:00 AM"));
        }
        catch(JSONException ex)
        {
            Log.e("MOCKDATA", "JSONException");
        }
    }
}

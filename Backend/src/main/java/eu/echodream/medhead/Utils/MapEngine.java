package eu.echodream.medhead.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.echodream.medhead.Models.Coordinate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MapEngine {
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371e3; // en mètres
        double lat1Rad = Math.toRadians(lat1);
            double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    public static Coordinate getCoordinates(String address, String city, String postcode) throws Exception {
        String query = URLEncoder.encode(address + " " + city + " " + postcode, StandardCharsets.UTF_8.toString());
        String url = "https://api-adresse.data.gouv.fr/search/?q=" + query;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();
        JsonObject data = gson.fromJson(response.toString(), JsonObject.class);

        JsonArray features = data.getAsJsonArray("features");

        if (features.size() > 0) {
            JsonArray coordinates = features.get(0).getAsJsonObject()
                    .getAsJsonObject("geometry")
                    .getAsJsonArray("coordinates");

            double lat = coordinates.get(1).getAsDouble();
            double lng = coordinates.get(0).getAsDouble();

            return new Coordinate(lat, lng);
        }

        return null;
    }
}

package com.ambeyindustry.iss;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class HttpUtil {

    //creates and return URL object from url string
    private static URL getURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException exc) {
            Log.e("HttpUtil: ", exc.toString());
        }
        return url;
    }

    //gets data from server
    private static String getDataFromServer(URL url) throws IOException {
        String responseString = "";
        if (url == null) {
            return responseString;
        }
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                stream = urlConnection.getInputStream();
                responseString = readFromStream(stream).toString();
            } else {
                Log.e("HttpUtil: ", "response code error: " + urlConnection.getResponseCode());
            }
        } catch (IOException exc) {
            responseString = "no internet";
            Log.e("HttpUtil: ", exc.toString());
        }
        return responseString;
    }

    //read data from network stream
    private static StringBuilder readFromStream(InputStream stream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (stream != null) {
            InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        return output;
    }

    private static List<Predictions> getPredictionsFromJSON(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        ArrayList<Predictions> predictions = new ArrayList<>();
        Predictions prediction;
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            JSONArray array = mainObject.getJSONArray("response");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.getJSONObject(i);
                prediction = new Predictions(object.getString("duration"), object.getString("risetime"));
                predictions.add(prediction);
            }
        } catch (JSONException exc) {
            Log.e("HttpUtil: ", exc.toString());
        }
        return predictions;
    }

    private static List<People> getPeopleFromJSON(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        ArrayList<People> peoples = new ArrayList<>();
        People people;
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            JSONArray array = mainObject.getJSONArray("people");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.getJSONObject(i);
                people = new People(object.getString("craft"), object.getString("name"));
                peoples.add(people);
            }
        } catch (JSONException exc) {
            Log.e("HttpUtil: ", exc.toString());
        }
        return peoples;
    }

    public static List<Predictions> getPredictionsFromServer(double lat, double lng) {
        URL url = getURL("http://api.open-notify.org/iss-pass.json?lat=" + Double.toString(lat) + "&lon=" + Double.toString(lng));
        String jsonString = "";
        try {
            jsonString = getDataFromServer(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getPredictionsFromJSON(jsonString);
    }

    public static List<People> getPeopleFromServer() {
        URL url = getURL("http://api.open-notify.org/astros.json");
        String jsonString = "";
        try {
            jsonString = getDataFromServer(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getPeopleFromJSON(jsonString);
    }
}

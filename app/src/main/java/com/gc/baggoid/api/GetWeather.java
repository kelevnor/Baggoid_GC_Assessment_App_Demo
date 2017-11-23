package com.gc.baggoid.api;

import android.os.AsyncTask;
import android.util.Log;
import com.gc.baggoid.Config;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marios Sifalakis on 8/23/17.
 * Class extends AsyncTask to retreive weather based on location
 * provided
 */
public class GetWeather extends AsyncTask<Void, Void, Void> {

    boolean completedCall = false;
    String response_passed = "";
    Double latitude;
    Double longitude;
    OnAsyncResult onAsyncResult;

    String baseUrl = "http://api.openweathermap.org/data/2.5/weather?";
    String latPrefix = "lat=";
    String longPrefix = "&lon=";
    String appIdPrefix = "&appid=";

    /**
     * @author Marios Sifalakis
     * @param latitude
     * @param longitude
     */
    public GetWeather(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setOnResultListener(OnAsyncResult onAsyncResult) {
        if (onAsyncResult != null) {
            this.onAsyncResult = onAsyncResult;
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        String urlStr = baseUrl
                + latPrefix + String.valueOf(latitude)
                + longPrefix + String.valueOf(longitude)
                + appIdPrefix + Config.WEATHER_API_KEY;

        Log.d(">>>URL", urlStr);


        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response_passed = urlConnectionInputStream(urlConnection);
                completedCall = true;
            }
            else{
                response_passed = urlConnectionErrorStream(urlConnection);
                completedCall = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {

//        Log.d(">>>API_RESPONSE", response_passed);

        if(completedCall){
            onAsyncResult.onResultSuccess(1, response_passed);
        }
        else {
            onAsyncResult.onResultFail(0, response_passed);
        }

    }
    @Override
    protected void onPreExecute() {
    }

    public interface OnAsyncResult {
        void onResultSuccess(int resultCode, String message);
        void onResultFail(int resultCode, String errorMessage);
    }


    private String urlConnectionInputStream(HttpURLConnection urlConnection) throws IOException{
        String response = "";
        InputStream is = new BufferedInputStream(urlConnection.getInputStream());
        response = readInputStream(is);
        return response;
    }

    private String urlConnectionErrorStream(HttpURLConnection urlConnection) throws IOException{
        String response = "";
        InputStream is = new BufferedInputStream(urlConnection.getErrorStream());
        response = readInputStream(is);
        return response;
    }

    private String readInputStream(InputStream is) throws IOException{
        StringBuilder builder = new StringBuilder();
        byte [] buffer = new byte[8192];
        int read;
        while ((read = is.read(buffer)) != -1)
        {
            builder.append(new String(buffer, 0, read, "UTF-8"));
        }
        return builder.toString();
    }

}
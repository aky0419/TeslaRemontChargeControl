package com.example.teslaremontchargecontrol;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostRequestToTesla extends AsyncTask<String, Void, String> {
    private static final String TAG = "PostRequestToTesla";

    public PostRequestToTesla() {
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            String requireInput= strings[1];

            //send post data request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(requireInput);
            writer.flush();

            //Read Server Response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();

            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.d(TAG, "doInBackground: data from server" + sb.toString());

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

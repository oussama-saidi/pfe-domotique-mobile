package com.pfedomotique.mobile.utils;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public  class PostData extends AsyncTask<String, Void, String> {
    private  String _urlApi = "";
    // on below line creating a variable for response line.
    String responseLine = null;
    // on below line creating a string builder.
    StringBuilder response = new StringBuilder();
    public PostData(String urlApi)
    {
        _urlApi = urlApi;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {

            // on below line creating a url to post the data.
            //URL url = new URL("http://192.168.1.28:8081/api/Compte/AjouterCompte");
            URL url = new URL(_urlApi);

            // on below line opening the connection.
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            // on below line setting method as post.
            client.setRequestMethod("POST");

            // on below line setting content type and accept type.
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("Accept", "application/json");

            // on below line setting client.
            client.setDoOutput(true);

            // on below line we are creating an output stream and posting the data.
            try (OutputStream os = client.getOutputStream()) {
                byte[] input = strings[0].getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // on below line creating and initializing buffer reader.
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(client.getInputStream(), "utf-8"))) {

                // on below line writing the response
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return  response.toString();
            }

        } catch (Exception e) {

            // on below line handling the exception.
            e.printStackTrace();
           // Toast.makeText(SignUpActivity.this, "Fail to post the data : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String s) {


        // dismiss the progress dialog after receiving data from API
        //progressDialog.dismiss();
        try {
            // JSON Parsing of data
            JSONArray jsonArray = new JSONArray(s);

            JSONObject oneObject = jsonArray.getJSONObject(0);
            // Pulling items from the array
            //title = oneObject.getString("title");



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
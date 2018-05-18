package com.login_signup_screendesign_demo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ListMovieActivity extends AppCompatActivity {
    MovieListAdapter movieListAdapter;
    ListView listView;
    String[] image;
    String[] name;
    String[] language;
    String[] id;
    String[] price;
    String[] rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);
        listView = findViewById(R.id.list_item);
        new GetMoviedetails().execute();
    }

    public class GetMoviedetails extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(ListMovieActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Connecting please wait...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                url = new URL("https://whiteoval.000webhostapp.com/mobileservices/moviedetails.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");


                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();


                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pdLoading.dismiss();
            Log.i("List : ", result);
            //Toast.makeText(RatefinderActivity.this, s, Toast.LENGTH_SHORT).show();
            if (result.equalsIgnoreCase(" ")) {


                Toast.makeText(ListMovieActivity.this, "error", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(ListMovieActivity.this, "OOPs! " +
                        "Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray ja = jsonObject.optJSONArray("item");
                    name = new String[ja.length()];
                    image = new String[ja.length()];
                    language = new String[ja.length()];
                    rating = new String[ja.length()];
                    id = new String[ja.length()];
                    price = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jb = ja.getJSONObject(i);

                        name[i] = (jb.getString("name"));
                        image[i] = (jb.getString("image"));
                        language[i] = (jb.getString("language"));
                        rating[i] = (jb.getString("rating"));
                        id[i] = (jb.getString("movieid"));

                        price[i] = (jb.getString("price"));


                        // Toast.makeText(ListItemActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }

                    movieListAdapter = new MovieListAdapter(ListMovieActivity.this, R.layout.list_movies,
                            name,
                            rating,
                            image,
                            language,
                            id, price);
                    listView.setAdapter(movieListAdapter);


/*
test bench
*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }


    }

}

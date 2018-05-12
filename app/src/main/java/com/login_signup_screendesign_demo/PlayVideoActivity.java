package com.login_signup_screendesign_demo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;

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

public class PlayVideoActivity extends AppCompatActivity {
    FullscreenVideoLayout videoLayout;
    SharedPreferences sharedPreferences;
    String[] videoUrl;
    String mid;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play_video);

        sharedPreferences = getSharedPreferences("Movie", MODE_PRIVATE);
        mid = sharedPreferences.getString("movieid", " null99");
        //     Toast.makeText(this, ""+mid, Toast.LENGTH_SHORT).show();
        videoLayout = findViewById(R.id.videoview);
        alertDialog = new AlertDialog.Builder(PlayVideoActivity.this
        ).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Payment Completed");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        new getVideoUrl().execute(mid);

                    }
                });
        alertDialog.show();
        ////////////////get video url /////////////


//        Toast.makeText(this, ""+videoUrl[0], Toast.LENGTH_SHORT).show();
     /*   videoLayout.setActivity(this);
       Uri videoUri = Uri.parse(videoUrl);
        try {
            videoLayout.setVideoURI(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlayVideoActivity.this, ListMovieActivity.class));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class getVideoUrl extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(PlayVideoActivity.this);
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
                url = new URL("https://whiteoval.000webhostapp.com/mobileservices/movieurl.php");
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


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("movieid", strings[0]);
                String query = builder.build().getEncodedQuery();


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
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

                // If username and password does not match display a error message
                Toast.makeText(PlayVideoActivity.this, "error", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(PlayVideoActivity.this, "OOPs! " +
                        "Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray ja = jsonObject.optJSONArray("item");
                    videoUrl = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jb = ja.getJSONObject(i);

                        videoUrl[i] = (jb.getString("video"));


                        //   Toast.makeText(PlayVideoActivity.this, videoUrl[0], Toast.LENGTH_SHORT).show();
                        videoLayout.setActivity(PlayVideoActivity.this);
                        Uri videoUri = Uri.parse(videoUrl[0]);
                        try {
                            videoLayout.setVideoURI(videoUri);
                            videoLayout.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    videoLayout.start();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(PlayVideoActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }


    }

}


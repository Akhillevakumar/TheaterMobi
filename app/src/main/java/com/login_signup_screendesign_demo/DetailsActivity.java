package com.login_signup_screendesign_demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class DetailsActivity extends AppCompatActivity {
    TextView mname, mlanguage, pro, dir, mscreen, mmusic;
    ImageView imageView;
    Button button1;
    String movname, lan, pr, di, ms, mu, imgurl;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        sharedPreferences = getSharedPreferences("Movie", MODE_PRIVATE);
        String mid = sharedPreferences.getString("movieid", "novalue");
        mname = findViewById(R.id.mname);
        mlanguage = findViewById(R.id.mlang);
        pro = findViewById(R.id.pro);
        dir = findViewById(R.id.dir);
        mscreen = findViewById(R.id.screen);
        mmusic = findViewById(R.id.music);
        imageView = findViewById(R.id.image);


        new Getdetails().execute(mid);

        button1 = findViewById(R.id.comment);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsActivity.this, CommentActivity.class));
            }
        });

    }

    public class Getdetails extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(DetailsActivity.this);
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
                url = new URL("https://whiteoval.000webhostapp.com/mobileservices/moviefulldetails.php");
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


                Toast.makeText(DetailsActivity.this, "error", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(DetailsActivity.this, "OOPs! " +
                        "Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray ja = jsonObject.optJSONArray("item");
                    /*name = new String[ja.length()];
                    image= new String[ja.length()];
                    language = new String[ja.length()];
                    rating = new String[ja.length()];
                    id= new String[ja.length()];*/
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jb = ja.getJSONObject(i);

                        movname = (jb.getString("name"));
                        pr = (jb.getString("producer"));
                        lan = (jb.getString("language"));
                        di = (jb.getString("director"));
                        ms = (jb.getString("screenplay"));
                        mu = (jb.getString("music"));
                        imgurl = (jb.getString("image"));


                        // Toast.makeText(ListItemActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                    mname.setText(movname);
                    mlanguage.setText(lan);
                    pro.setText(pr);
                    dir.setText(di);
                    mscreen.setText(ms);
                    mmusic.setText(mu);
                    Picasso.with(DetailsActivity.this)
                            .load(imgurl)
                            .into(imageView);




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

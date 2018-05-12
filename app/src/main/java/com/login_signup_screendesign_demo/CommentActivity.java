package com.login_signup_screendesign_demo;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
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

public class CommentActivity extends AppCompatActivity {
    ListView commentList;
    String[] usernameforlisting;
    String[] comments;
    EditText incomment;
    Button send;
    RatingBar ratingBar11;
    ListCommentsAdapter listCommentsAdapter;
    SharedPreferences sharedPreferences;
    String myCommment, myRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentList = findViewById(R.id.com);
        sharedPreferences = getSharedPreferences("Movie", MODE_PRIVATE);
        final String mid = sharedPreferences.getString("movieid", "novalue");
        final String mail = sharedPreferences.getString("email", "nomail");
        //Toast.makeText(this, ""+mid+"\n"+mail, Toast.LENGTH_SHORT).show();
        new GetComments().execute(mid);
        incomment = findViewById(R.id.commentin);
        send = findViewById(R.id.sent);
        ratingBar11 = findViewById(R.id.ratingBar1);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCommment = incomment.getText().toString();
                Float rate = ratingBar11.getRating();
                myRating = Float.toString(rate);
                // Toast.makeText(CommentActivity.this, ""+rate+myCommment, Toast.LENGTH_SHORT).show();
                new MyComments().execute(mid, mail, myRating, myCommment);
            }
        });


    }

    public class GetComments extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(CommentActivity.this);
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
                url = new URL("https://whiteoval.000webhostapp.com/mobileservices/getcomments.php");
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


                Toast.makeText(CommentActivity.this, "N0comments", Toast.LENGTH_LONG).show();


            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(CommentActivity.this, "OOPs! " +
                        "Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray ja = jsonObject.optJSONArray("item");
                    usernameforlisting = new String[ja.length()];
                    comments = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jb = ja.getJSONObject(i);


                        comments[i] = (jb.getString("comments"));
                        usernameforlisting[i] = (jb.getString("username"));


                        // Toast.makeText(ListItemActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }

                    listCommentsAdapter = new ListCommentsAdapter(CommentActivity.this, R.layout.list_comments,
                            usernameforlisting, comments);
                    commentList.setAdapter(listCommentsAdapter);


/*
test bench
*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }


    }

    public class MyComments extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(CommentActivity.this);
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
                url = new URL("https://whiteoval.000webhostapp.com/mobileservices/putcomments.php");
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


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("mid", strings[0]);
                builder.appendQueryParameter("email", strings[1]);
                builder.appendQueryParameter("rating", strings[2]);
                builder.appendQueryParameter("comment", strings[3]);


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

            if (result.contains("success")) {

                startActivity(getIntent());
            } else if (result.contains("error")) {
                Toast.makeText(CommentActivity.this, "Error while sending", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommentActivity.this, "Connection problem", Toast.LENGTH_SHORT).show();
            }

/*
test bench
*/


        }


    }


}



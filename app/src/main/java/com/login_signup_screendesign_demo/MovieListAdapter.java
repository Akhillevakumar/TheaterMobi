package com.login_signup_screendesign_demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieListAdapter extends ArrayAdapter {
    String[] mname, mrate, murl, price, mlanguage, mid;
    Context contextcurrent;
    ImageView poster;
    TextView pricedata, moviename, language, idm;
    RatingBar movieratingBar;
    Button viewMovie, movieDetails;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    URL url;
    int a = 0, b = 0;

    public MovieListAdapter(@NonNull Context context,
                            int resource,
                            String[] moviename,
                            String[] movierating,
                            String[] movieimgurl,
                            String[] language,
                            String[] movieid,
                            String[] price) {
        super(context, resource, moviename);
        this.mname = moviename;
        this.mrate = movierating;
        this.murl = movieimgurl;
        this.mlanguage = language;
        this.mid = movieid;
        this.price = price;
        this.contextcurrent = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) contextcurrent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.list_movies, parent, false);
        poster = view.findViewById(R.id.poster);
        moviename = view.findViewById(R.id.moviename);
        language = view.findViewById(R.id.language);
        //idm=(TextView)view.findViewById(R.id.imid);
        movieratingBar = view.findViewById(R.id.ratingBar);
        viewMovie = view.findViewById(R.id.viewbtn);
        movieDetails = view.findViewById(R.id.details);
        pricedata = view.findViewById(R.id.dataprice);
        pricedata.setText(price[position]);
        moviename.setText(mname[position]);
        language.setText(mlanguage[position]);
        // idm.setText(mid[position]);
        //idm.setVisibility(View.INVISIBLE);
        sharedPreferences = getContext().getSharedPreferences("Movie", Context.MODE_PRIVATE);
        /////////////////////////////////////////////////image//////////////////////////////////////////////
////////////failed try on bitmap factory///////////////
      /*       try {
            url=new URL(String.valueOf(murl[position]));
            Bitmap bitmap= BitmapFactory.decodeStream(url.openConnection().getInputStream());
            poster.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(contextcurrent, ""+e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/
/////////////////////////////////////////////////////
        Picasso.with(getContext())
                .load(murl[position])
                .into(poster);
        ////////////////////////////////////////////////rating//////////////////////////////////////////////
        // Toast.makeText(contextcurrent, ""+mrate[position], Toast.LENGTH_SHORT).show();
        if (mrate[position] == "null") {
            mrate[position] = "1.0";
            movieratingBar.setRating(Float.parseFloat(mrate[position]));
        } else {
            movieratingBar.setRating(Float.parseFloat(mrate[position]));
        }
        ///////////////////////////////////////////////////rest code////////////////////////////////////////

        viewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieid = mid[position];
                //  Toast.makeText(contextcurrent, ""+movieid, Toast.LENGTH_SHORT).show();


//////////////////save movie id//////////////////////
                editor = sharedPreferences.edit();
                editor.putString("movieid", movieid);
                editor.commit();

                //////if flag is zero
                ///////////////////////////////set coundowntimer ////////////////////

                ////if///////////
                /// timer value is not equal to zero /////////////
                ///////////////////////////navigate to player activity.//////////////
                /////else////////
                /// go to payment activity///////////////
                /// ////
                ////////////


                if (a == 0) {
                    new CountDownTimer(30000, 1000) {

                        @Override
                        public void onTick(long l) {
                            final int a = 1;
                            final int b = 1;
                        }

                        @Override
                        public void onFinish() {
                            final int a = 0;
                            final int b = 0;
                        }
                    }.start();
                }

                getContext().startActivity(new Intent(getContext(), PaymentActivity.class));
            }
        });
        movieDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mviid = mid[position];
                editor = sharedPreferences.edit();
                editor.putString("movieid", mviid);
                editor.commit();
                ///////////////////// goto details activity/////////////////////////////
                getContext().startActivity(new Intent(getContext(), DetailsActivity.class));
            }
        });


        return view;
    }


}

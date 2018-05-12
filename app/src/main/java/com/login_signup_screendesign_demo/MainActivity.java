package com.login_signup_screendesign_demo;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {
    private static FragmentManager fragmentManager;
    private ImageView mFbLogoImageView;

    private ImageView mFbLogoStaticImageView;
    private Boolean ANIMATION_ENDED = false;
    private Boolean START_ANIMATION = true;

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            START_ANIMATION = savedInstanceState.getBoolean("START_ANIMATION");
        }
        mFbLogoImageView = findViewById(R.id.fbLogoImageView);
        mFbLogoStaticImageView = findViewById(R.id.fbLogoStaticImageView);

        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment

        //LoginFragment  ivide ondeeeeee///////////////////////////////////// ivide ivide///////////

		/*if (savedInstanceState == null) {
			fragmentManager
					.beginTransaction()
					.replace(R.id.frameContainer, new Login_Fragment(),
							Utils.Login_Fragment).commit();
		}*/

////////////////////////////////////////////////////////////////////////////////////////////////////


        if (START_ANIMATION) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                // mFbCoverImageView.setVisibility(View.GONE);

                mFbLogoStaticImageView.setVisibility(View.GONE);


            Animation moveFBLogoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_fb_logo);
            moveFBLogoAnimation.setFillAfter(true);
            moveFBLogoAnimation.setAnimationListener(this);
            mFbLogoImageView.startAnimation(moveFBLogoAnimation);

        } else {
            mFbLogoImageView.setVisibility(View.GONE);
        }

        final View activityRootView = findViewById(R.id.mainConstraintLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (ANIMATION_ENDED || !START_ANIMATION) {

                    int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                    if (heightDiff > dpToPx(MainActivity.this, 200)) {
                        //Soft keyboard is shown
                        /*if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        }*/
                        // mFbCoverImageView.setVisibility(View.GONE);

                    } else {
                        //Soft keyboard is hidden
                       /* if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        }
                        //mFbCoverImageView.setVisibility(View.VISIBLE);
*/
                    }
                }
            }
        });
    }

    // Replace Login Fragment with animation
    protected void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new Login_Fragment(),
                        Utils.Login_Fragment).commit();
    }

    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Utils.SignUp_Fragment);
        Fragment ForgotPassword_Fragment = fragmentManager
                .findFragmentByTag(Utils.ForgotPassword_Fragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (SignUp_Fragment != null)
            replaceLoginFragment();
        else if (ForgotPassword_Fragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameContainer, new Login_Fragment(),
                                Utils.Login_Fragment).commit();
            }
        }, 500);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

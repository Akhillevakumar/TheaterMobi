package com.login_signup_screendesign_demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.HttpURLConnection.HTTP_OK;

public class Login_Fragment extends Fragment implements OnClickListener {
    private static View view;

    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    String getEmailId, getPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Login_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);
        initViews();
        setListeners();
        sharedPreferences = getContext().getSharedPreferences("Movie", Context.MODE_PRIVATE);
        return view;
    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        emailid = view.findViewById(R.id.login_emailid);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        loginButton.setText("Next");
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.createAccount);
        show_hide_password = view.findViewById(R.id.show_hide_password);
        show_hide_password.setVisibility(View.INVISIBLE);
        loginLayout = view.findViewById(R.id.login_layout);
        password.setVisibility(View.INVISIBLE);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                loginButton.setText("Next");
                checkEmailValidation();
                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignUp_Fragment(),
                                Utils.SignUp_Fragment).commit();
                break;
        }

    }

    private void checkEmailValidation() {
        getEmailId = emailid.getText().toString();


        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter Email");

        }
        // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            // Else do login and do your stuff
        else {
            Toast.makeText(getActivity(), "Enter password.", Toast.LENGTH_SHORT)
                    .show();
            loginButton.setText("Login");
            emailid.setVisibility(View.INVISIBLE);
            password.setVisibility(View.VISIBLE);
            show_hide_password
                    .setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkValidation();
                }
            });
        }
    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password

        getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);


        // Check for both field is empty or not
        if (getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter password");

        }
        // Check if email id is valid or not

        // Else do login and do your stuff
        else
			/*Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
					.show();
		Toast.makeText(getContext(), getEmailId+"\n"+getPassword, Toast.LENGTH_SHORT).show();*/
            new DoLogin().execute(getEmailId, getPassword);

    }

    private class DoLogin extends AsyncTask<String, String, String> {

        ProgressDialog pd = new ProgressDialog(getContext());
        HttpURLConnection connection;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                url = new URL("https://whiteoval.000webhostapp.com/mobileservices/login.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("email", strings[0]);
                builder.appendQueryParameter("password", strings[1]);


                String query = builder.build().getEncodedQuery();

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                connection.connect();

                int rc = connection.getResponseCode();
                if (rc == HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                } else
                    return "unsuccessfull";

            } catch (IOException e) {
                e.printStackTrace();
                return "Exception";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();
			/*if(s.equals("invalid"))
			{
				Toast.makeText(getContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
				fragmentManager
						.beginTransaction()
						.replace(R.id.frameContainer, new Login_Fragment(),
								Utils.Login_Fragment).commit();
			}
			else if (s.equalsIgnoreCase("exception") || s.equalsIgnoreCase("unsuccessfull")) {

				Toast.makeText(getContext(), "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

			}

			else if(s.equals("error")) {
				Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
			}
			else {
			    startActivity(new Intent(getContext(),ListMovieActivity.class));
			}*/
            if (s.contains("sucess")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                editor = sharedPreferences.edit();
                editor.putString("email", getEmailId);
                editor.commit();
                startActivity(new Intent(getContext(), ListMovieActivity.class));

            } else if (s.contains("Exception") || s.contains("unsuccessful")) {

                Toast.makeText(getContext(), "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameContainer, new Login_Fragment(),
                                Utils.Login_Fragment).commit();

            } else {
                Toast.makeText(getContext(), "PHP Error!", Toast.LENGTH_SHORT).show();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameContainer, new Login_Fragment(),
                                Utils.Login_Fragment).commit();

            }
        }

    }

}

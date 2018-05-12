package com.login_signup_screendesign_demo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    String getFullName;
    String getEmailId;
    String getMobileNumber;
    String getLocation;
    String getPassword;
    String getConfirmPassword;

    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);

        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fullName = view.findViewById(R.id.fullName);
        emailId = view.findViewById(R.id.userEmailId);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        location = view.findViewById(R.id.location);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.already_user);
        terms_conditions = view.findViewById(R.id.terms_conditions);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new MainActivity().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        getFullName = fullName.getText().toString();
        getEmailId = emailId.getText().toString();
        getMobileNumber = mobileNumber.getText().toString();
        getLocation = location.getText().toString();
        getPassword = password.getText().toString();
        getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please select Terms and Conditions.");

            // Else do signup
        else

            new AddUser().execute(getFullName, getEmailId, getMobileNumber, getLocation, getConfirmPassword);

    }

    public class AddUser extends AsyncTask<String, String, String> {
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
                url = new URL("https://whiteoval.000webhostapp.com/mobileservices/signup.php");
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
                builder.appendQueryParameter("name", strings[0]);
                builder.appendQueryParameter("email", strings[1]);
                builder.appendQueryParameter("mobile", strings[2]);
                builder.appendQueryParameter("location", strings[3]);
                builder.appendQueryParameter("password", strings[4]);


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
                return "Error!";
            }

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();

            if (s.contains("success")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                Login_Fragment fragment = new Login_Fragment();

                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, fragment);
                fragmentTransaction.commit();
            } else if (s.contains("Exception") || s.contains("unsuccessful")) {

                Toast.makeText(getContext(), "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if (s.contains("alreadyreg")) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("Registration");
                ad.setMessage("You are already registered");
                ad.setCancelable(false);
                ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog al = ad.create();
                ad.show();
            } else {
                Toast.makeText(getContext(), "PHP Error!", Toast.LENGTH_SHORT).show();

            }
        }
    }
}

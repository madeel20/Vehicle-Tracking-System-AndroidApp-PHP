package com.mfe.madeel.devicetracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText username , password;
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 100;
    TextView textPHP;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
       
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);




    }

    public void onregclick(View v){
        Intent activityChangeIntent = new Intent(MainActivity.this,register.class);
        // currentContext.startActivity(activityChangeIntent);
        MainActivity.this.startActivity(activityChangeIntent);
    }
        //Make call to AsyncRetrieve
    public void onclickbtn(View v) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(user.isEmpty() || pass.isEmpty()){

            Toast.makeText(MainActivity.this,
                    "Fill all the fields!", Toast.LENGTH_LONG).show();
        }
        else {

            //http://adeel20.000webhostapp.com/register.php?key=1234&username=kashif&password=1234&cdate=2018-9-3
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("mfenter.com")

                    .appendPath("tracker")

                    .appendPath("checklogin.php")
                    .appendQueryParameter("key", "1345")
                    .appendQueryParameter("username",user)
                    .appendQueryParameter("password",pass);

            String myUrl = builder.build().toString();
            AsyncRetrieve as = new AsyncRetrieve();
as.execute();


            as.u = myUrl;



        }

    }


    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;
        String u;
        String res;
        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLogging In...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        // This method does not interact with UI, You need to pass result to onPostExecute to display
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL(u);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
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
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(String result) {



            // you to understand error returned from doInBackground method
            result.trim();

            res = result.toString();
            String user = username.getText().toString();


            if(res.equalsIgnoreCase("Succeed") ){



                AsyncRetrieve1 as =new AsyncRetrieve1();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("mfenter.com")

                        .appendPath("tracker")

                        .appendPath("getauid.php")
                        .appendQueryParameter("key", "1345")
                        .appendQueryParameter("username",user);


                String myUrl = builder.build().toString();

                as.execute();


                as.u = myUrl;

            }
            else if(res.equalsIgnoreCase("na") ){
                Toast.makeText(MainActivity.this, "User is no longer Active!", Toast.LENGTH_LONG).show();
            }
            else  {
                //Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "Username or password is incorrect!", Toast.LENGTH_LONG).show();

            }
            pdLoading.dismiss();

        }

    }






    private class AsyncRetrieve1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;
        String u;
        String res;
        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLogging In...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        // This method does not interact with UI, You need to pass result to onPostExecute to display
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL(u);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
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
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(String result) {
             pdLoading.dismiss();


            // you to understand error returned from doInBackground method
            result.trim();

            res = result.toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("AUId" , res);
            editor.commit();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("mfenter.com")

                    .appendPath("tracker")
                    .appendPath("checkapp.php")
                    .appendQueryParameter("key", "1345")
                    .appendQueryParameter("AUId", sharedpreferences.getString("AUId",""));


            String myUrl = builder.build().toString();

            AsyncRetrieve2 as = new AsyncRetrieve2();
            as.u = myUrl;
            as.execute();



        }


    }




private class AsyncRetrieve2 extends AsyncTask<String, String, String> {
    ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
    HttpURLConnection conn;
    URL url = null;
    String u;
    String res;
    //this method will interact with UI, here display loading message
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pdLoading.setMessage("\tLogging In...");
        pdLoading.setCancelable(false);
        pdLoading.show();

    }

    // This method does not interact with UI, You need to pass result to onPostExecute to display
    @Override
    protected String doInBackground(String... params) {
        try {
            // Enter URL address where your php file resides
            url = new URL(u);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        }
        try {

            // Setup HttpURLConnection class to send and receive data from php
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // setDoOutput to true as we recieve data from json file
            conn.setDoOutput(true);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return e1.toString();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
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
            return e.toString();
        } finally {
            conn.disconnect();
        }


    }

    // this method will interact with UI, display result sent from doInBackground method
    @Override
    protected void onPostExecute(String result) {
        pdLoading.dismiss();
        result.trim();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(result.equals("1")){
            sharedpreferences.edit().putString("Approval","1").commit();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("username" , user);
                editor.putString("password", pass);
                editor.commit();
            Intent activityChangeIntent = new Intent(MainActivity.this, login.class);
            // currentContext.startActivity(activityChangeIntent);
            finishAffinity();

            startActivity(activityChangeIntent);

        }
        else{
            sharedpreferences.edit().putString("Approval","0").commit();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("username" , user);
            editor.putString("password", pass);
            editor.commit();
            Intent activityChangeIntent = new Intent(MainActivity.this, Approve.class);
            // currentContext.startActivity(activityChangeIntent);
            finishAffinity();

            startActivity(activityChangeIntent);
        }




}

}}


package com.mfe.madeel.devicetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Approve extends AppCompatActivity {
TextView t ;
    private boolean approved=false;
    SharedPreferences  sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
         t = (TextView)findViewById(R.id.textView6);
        sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);
        t.setText(""+sharedpreferences.getString("username",""));
        repeatcheck();

    }

    public void checkApproval(){
        sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);
///checkapp.php?key=1345&AUId=61
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("mfenter.com")

                .appendPath("tracker")
                .appendPath("checkapp.php")
                .appendQueryParameter("key", "1345")
                .appendQueryParameter("AUId", sharedpreferences.getString("AUId",""));


        String myUrl = builder.build().toString();

            AsyncRetrieve as = new AsyncRetrieve();
        as.u = myUrl;
        as.execute();






    }
    Handler handler;
    public void repeatcheck(){
        handler = new Handler();
        final Timer timer = new Timer();
        final TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            checkApproval();
                            if(approved==true){
                                timer.cancel();
                                sharedpreferences.edit().putString("Approval","1").commit();
                                Toast.makeText(Approve.this, "Approved!", Toast.LENGTH_LONG).show();
                                Intent activityChangeIntent = new Intent(Approve.this, login.class);
                                // currentContext.startActivity(activityChangeIntent);

                                Approve.this.startActivity(activityChangeIntent);
                                finishAffinity();
                                finish();
                            }

                        } catch (Exception e) {
                           // Toast.makeText(Approve.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 100 ); //execute in every 50000 ms
    }

    public  void logout1(View view){
        Intent stopServiceIntent = new Intent(this, MyService.class);
        stopService(stopServiceIntent);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent activityChangeIntent = new Intent(Approve.this,MainActivity.class);

        // currentContext.startActivity(activityChangeIntent);
        finishAffinity();
        startActivity(activityChangeIntent);
    }


    public class AsyncRetrieve extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        String u;
        String res;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


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

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                cancel(true);
                //Toast.makeText(Approve.this, e1.toString(), Toast.LENGTH_LONG).show();
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

            } catch (Exception e) {
                cancel(true);
               // Toast.makeText(register.this, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(Approve.this, result.trim(), Toast.LENGTH_LONG).show();
result.trim();
  if(result.equals("1")){

      approved =true;

  }




        }

            /*t2.setText(result.toString());
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("id" , result.toString());
            sendgps();
           /* res = result.toString();
            result.trim();
            if(res.equalsIgnoreCase("Succeed") ){

                Toast.makeText(login.this, "Log In Succesfull!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(login.this, "Username or password is incorrect!", Toast.LENGTH_LONG).show();
            }*/

    }
}

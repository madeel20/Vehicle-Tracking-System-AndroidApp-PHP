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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class register extends AppCompatActivity {
EditText username,password,rpassword,firstname,lastname,address,cellno,brokerno,cnic,vehicleno,partyname;
    Button uploadbtn;
    public static final int CONNECTION_TIMEOUT = 50000;
    public static final int READ_TIMEOUT = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        username =(EditText)findViewById(R.id.txtuserame);
        password =(EditText)findViewById(R.id.txtpass);
        rpassword =(EditText)findViewById(R.id.txtrpass);

        firstname =(EditText)findViewById(R.id.txtfname);
        lastname =(EditText)findViewById(R.id.txtlname);
        address =(EditText)findViewById(R.id.txtAddress);
        cellno =(EditText)findViewById(R.id.txtCEllno);
        brokerno =(EditText)findViewById(R.id.txtBrokername);
        cnic =(EditText)findViewById(R.id.txtcnic);
        vehicleno =(EditText)findViewById(R.id.txtVehicleno);
        partyname =(EditText)findViewById(R.id.txtpartyname);




    }



    public void regtbn(View v){
       if(password.getText().toString().isEmpty() ||  rpassword.getText().toString().isEmpty())
       {
           Toast.makeText(register.this, "password is required!", Toast.LENGTH_LONG).show();
       }
       else if(password.getText().toString().length()<4 || rpassword.getText().toString().length()<4){
           Toast.makeText(register.this, "Password should contain atleast 4 characters!", Toast.LENGTH_LONG).show();
       }
        else {

//register.php?key=1234&username=ayesha&password=1234&cdate=2018-9-3

            if(password.getText().toString().equals(rpassword.getText().toString())){
                Date c = Calendar.getInstance().getTime();
                //System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c);
                //Toast.makeText(register.this, formattedDate, Toast.LENGTH_LONG).show();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("mfenter.com")

                        .appendPath("tracker")
                        .appendPath("register.php")
                        .appendQueryParameter("key","1234")
                        .appendQueryParameter("username", username.getText().toString())
                        .appendQueryParameter("password", password.getText().toString())
                        .appendQueryParameter("cdate",formattedDate);


                String myUrl = builder.build().toString();
                AsyncRetrieve as = new AsyncRetrieve();
                as.execute();
                as.u = myUrl;





            }
            else {
                Toast.makeText(register.this, "Passwords does not matched!", Toast.LENGTH_LONG).show();
            }

        }

    }


    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(register.this);
        HttpURLConnection conn;
        URL url = null;
        String u;
        String res;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tCreating Account...");
            pdLoading.setCancelable(true);
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
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Toast.makeText(register.this, e1.toString(), Toast.LENGTH_LONG).show();
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
            pdLoading.dismiss();

         SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);

         if (result.toString().equalsIgnoreCase("1")) {
             // you to understand error returned from doInBackground method
             sharedpreferences = getSharedPreferences(MainActivity.mypreference,
                     Context.MODE_PRIVATE);
             //   Toast.makeText(register.this, "Registration Succesfull!", Toast.LENGTH_LONG).show();
             SharedPreferences.Editor editor = sharedpreferences.edit();
             editor.putString("username", username.getText().toString());
             editor.putString("password", password.getText().toString());
             editor.commit();
             AsyncRetrieve1 as = new AsyncRetrieve1();
             Uri.Builder builder = new Uri.Builder();
             builder.scheme("https")
                     .authority("mfenter.com")

                     .appendPath("tracker")

                     .appendPath("getauid.php")
                     .appendQueryParameter("key", "1345")
                     .appendQueryParameter("username", username.getText().toString());


             String myUrl = builder.build().toString();

             as.execute();


             as.u = myUrl;

         } else {
             Toast.makeText(register.this, result.toString(), Toast.LENGTH_LONG).show();

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




    private class AsyncRetrieve1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(register.this);
        HttpURLConnection conn;
        URL url = null;
        String u;
        String res;

        public AsyncRetrieve1() {

        }

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tUploading details..");
            pdLoading.setCancelable(true);
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
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);

            // you to understand error returned from doInBackground method
            result.trim();

            res = result.toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("AUId", res);
            editor.putString("Approval","0");
            editor.putString("username",username.getText().toString());
            editor.commit();
            login.approval=0;
            //Toast.makeText(register.this, res, Toast.LENGTH_LONG).show();
            //insertdetails.php?key=1234&auid=108&fname=adeel&lname=khan&address=5-e%20NOrth%20karachi&cellno=94504890&vehicleno=859049&brokerno=8880&cnic=49508940329&pname=asjdkfl&pic=409jkalnocfsd
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("mfenter.com")

                    .appendPath("tracker")
                    .appendPath("insertdetails.php")
                    .appendQueryParameter("key", "1234")
                    .appendQueryParameter("auid", res)
                    .appendQueryParameter("fname", firstname.getText().toString())
                    .appendQueryParameter("lname", lastname.getText().toString())
                    .appendQueryParameter("address", address.getText().toString())
                    .appendQueryParameter("cellno", cellno.getText().toString())
                    .appendQueryParameter("vehicleno", vehicleno.getText().toString())
                    .appendQueryParameter("brokerno", brokerno.getText().toString())
                    .appendQueryParameter("cnic", cnic.getText().toString())
                    .appendQueryParameter("pname", partyname.getText().toString());
            String myUrl = builder.build().toString();
            AsyncRetrieve2 as = new AsyncRetrieve2();
            as.u = myUrl;
            as.execute();



        }

    }
private class AsyncRetrieve2 extends AsyncTask<String, String, String> {
    ProgressDialog pdLoading = new ProgressDialog(register.this);
    HttpURLConnection conn;
    URL url = null;
    String u;
    String res;

    public AsyncRetrieve2() {

    }

    //this method will interact with UI, here display loading message
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pdLoading.setMessage("\tUploading details..");
        pdLoading.setCancelable(true);
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
            cancel(true);
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

            cancel(true);
            e.printStackTrace();
            return e.toString();
        } finally {
            conn.disconnect();
        }


    }

    SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);

    // this method will interact with UI, display result sent from doInBackground method
    @Override
    protected void onPostExecute(String result) {
        pdLoading.dismiss();



        Toast.makeText(register.this, "Account Created!!", Toast.LENGTH_LONG).show();
        Intent activityChangeIntent = new Intent(register.this, Approve.class);

        // currentContext.startActivity(activityChangeIntent);
        finishAffinity();
        finish();

        register.this.startActivity(activityChangeIntent);



    }

}}




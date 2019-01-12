package com.mfe.madeel.devicetracker;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Timer;
import java.util.TimerTask;

public class login extends AppCompatActivity {
    public static int approval=1;
    private GPSTracker gpsTracker;
    TextView t,fname,lname,address,cellno,vehicleno,brokerno,cnic,partyname;
   static  SharedPreferences sharedpreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fname = (TextView)findViewById(R.id.txtfname);
        lname = (TextView)findViewById(R.id.txtlname);
        address = (TextView)findViewById(R.id.txtAddress);
        cellno = (TextView)findViewById(R.id.txtCEllno);
        vehicleno = (TextView)findViewById(R.id.txtVehicleno);
        brokerno = (TextView)findViewById(R.id.txtBrokername);
        cnic = (TextView)findViewById(R.id.txtcnic);
        partyname = (TextView)findViewById(R.id.txtpartyname);
        sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);
        //Toast.makeText(login.this, sharedpreferences.getString("Approval",""), Toast.LENGTH_LONG).show();


        if(sharedpreferences.getString("Approval","").equals("0")){
            Intent activityChangeIntent = new Intent(login.this, Approve.class);

            // currentContext.startActivity(activityChangeIntent);
            finishAffinity();

            login.this.startActivity(activityChangeIntent);


        }
        else {
            try {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }
                else {
                    if (!isMyServiceRunning(MyService.class)) {
                        Intent myServiceIntent = new Intent(this, MyService.class);
                        startService(myServiceIntent);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);
            t = (TextView) findViewById(R.id.username);
            t.setText( " Username: "+ sharedpreferences.getString("username", ""));


            final Handler handler = new Handler();
            final Timer timer = new Timer();
            final TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                AsyncRetrieve3 as = new AsyncRetrieve3();
                                //"loaddetails.php?key=1234&AUId=52";
                                String Auiid =  sharedpreferences.getString("AUId", "");

                                Uri.Builder builder1 = new Uri.Builder();

                                builder1.scheme("https")
                                        .authority("mfenter.com")

                                        .appendPath("tracker")

                                        .appendPath("loaddetails.php")
                                        .appendQueryParameter("key","1234")
                                        .appendQueryParameter("AUId", Auiid);

                                String myUrl1 = builder1.build().toString();
                                as.u = myUrl1;

                                as.execute();
                            } catch (Exception e) {
                               // Toast.makeText(Approve.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 30000 );
                //loaddetails.php?key=1234&AUId=52



            //GPSTracker g = new GPSTracker(this);
            //Toast.makeText(login.this, sharedpreferences.getString("Approval","").equals("0"), Toast.LENGTH_LONG).show();


        }

        //callAsynchronousTask();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!isMyServiceRunning(MyService.class)) {
                        Intent myServiceIntent = new Intent(this, MyService.class);
                        startService(myServiceIntent);
                    }

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                   // Toast.makeText(login.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    //finish();
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    }
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void checkGPSStatus() {
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Location Service is not enabled! Please Enable it.");
        dialog.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
                //get gps
            }
        });
        dialog.setCancelable(true);
        AlertDialog testdialog = dialog.create();
        if(!gps_enabled && !network_enabled) {
            // notify user



            testdialog.show();

        }
        else{
            testdialog.dismiss();
               }
    }
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                          checkGPSStatus();
                        } catch (Exception e) {
                           // Toast.makeText(login.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 6000); //execute in every 50000 ms
    }
    public void sendgps() {

            Date c = Calendar.getInstance().getTime();
            //System.out.println("Current time => " + c);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate  = fmt.format(c);


        //Toast.makeText(login.this, formattedDate, Toast.LENGTH_LONG).show();
            String s =  sharedpreferences.getString("AUId", "");
            AsyncRetrieve2 sa = new AsyncRetrieve2();
            Uri.Builder builder1 = new Uri.Builder();
            GPSTracker gps = new GPSTracker(this);
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            //getgps.php/?key=1345&lat=45&lon=55&AUId=2&lup=2018-09-04%2002:04:20
            builder1.scheme("https")
                    .authority("mfenter.com")

                    .appendPath("tracker")

                    .appendPath("getgps.php")
                    .appendQueryParameter("key","1345")
                    .appendQueryParameter("AUId", s)
                    .appendQueryParameter("lat", String.valueOf(latitude))
                    .appendQueryParameter("lon",String.valueOf(longitude))
            .appendQueryParameter("lup", formattedDate );
            String myUrl1 = builder1.build().toString();
            sa.u = myUrl1;
            sa.execute();
           // Toast.makeText(login.this, String.valueOf(latitude), Toast.LENGTH_LONG).show();

    }
    public  void logout(View view){
        Intent stopServiceIntent = new Intent(this, MyService.class);
        stopService(stopServiceIntent);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent activityChangeIntent = new Intent(login.this,MainActivity.class);

        // currentContext.startActivity(activityChangeIntent);
        finishAffinity();
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(1);
      login.this.startActivity(activityChangeIntent);
    }
    public class AsyncRetrieve2 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(login.this);
        HttpURLConnection conn;
        URL url = null;
        String u;
        String res;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

         /*  pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();*/

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

            //pdLoading.dismiss();

            // you to understand error returned from doInBackground method
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
    public class AsyncRetrieve3 extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;
        String u;
        String res;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

         /*  pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();*/

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

            //pdLoading.dismiss();

            // you to understand error returned from doInBackground method
           /* res = result.toString();
            result.trim();
            if(res.equalsIgnoreCase("Succeed") ){

                Toast.makeText(login.this, "Log In Succesfull!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(login.this, "Username or password is incorrect!", Toast.LENGTH_LONG).show();
            }*/
            try {
                JSONObject obj = new JSONObject(result);
                fname.setText("First Name: "+obj.get("firstname").toString());
                lname.setText("Last Name: "+obj.get("lastname").toString());
                address.setText("Address: "+obj.get("address").toString());
                cellno.setText("Cell No: "+obj.get("cellno").toString());
                vehicleno.setText("Vehicle No: "+obj.get("vehicleno").toString());
                brokerno.setText("Broker No: "+ obj.get("brokerno").toString());
                cnic.setText("CNIC No: "+ obj.get("cnic").toString());
                partyname.setText("Party Name: "+ obj.get("partyname").toString());

                if(obj.get("status").toString().trim().equals("0")){
                    Toast.makeText(login.this, "User no longer active!", Toast.LENGTH_LONG).show();
                    Intent stopServiceIntent = new Intent(login.this, MyService.class);
                    stopService(stopServiceIntent);
                    SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent activityChangeIntent = new Intent(login.this,MainActivity.class);

                    // currentContext.startActivity(activityChangeIntent);
                    finishAffinity();
                    NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    nm.cancel(1);
                    login.this.startActivity(activityChangeIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }}


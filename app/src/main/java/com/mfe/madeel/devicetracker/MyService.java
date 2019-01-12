package com.mfe.madeel.devicetracker;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Belal on 12/30/2016.
 */

public class MyService extends Service {
    //SharedPreferences sharedpreferences ;
    String s;
    Timer timer1;
    public MyService() {

      //  sharedpreferences = getSharedPreferences(MainActivity.mypreference, Context.MODE_PRIVATE);
    }
    void  getid(String s){
        this.s=s;

    }


        private void showNotification( ) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            int iconId = R.drawable.logo;
            int uniqueCode = new Random().nextInt(Integer.MAX_VALUE);
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(iconId)
                    .setContentTitle("MFE Vehicle Tracker")
                    .setContentText("Tracking Location of your device...")
                    .setContentIntent(pendingIntent).build();
            startForeground(uniqueCode, notification);



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
                            sendgps();
                            // Toast.makeText(null,"sdfsdf", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 15000);

    }
    public void sendgps() {

        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate  = fmt.format(c);


        //Toast.makeText(login.this, formattedDate, Toast.LENGTH_LONG).show();
        //String s =
        AsyncRetrieve2 sa = new AsyncRetrieve2();
        Uri.Builder builder1 = new Uri.Builder();
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        //getgps.php/?key=1345&lat=45&lon=55&AUId=2&lup=2018-09-04%2002:04:20
        if(latitude > 22 || longitude>22) {
            builder1.scheme("https")
                    .authority("mfenter.com")

                    .appendPath("tracker")

                    .appendPath("getgps.php")
                    .appendQueryParameter("key", "1345")
                    .appendQueryParameter("AUId", login.sharedpreferences.getString("AUId", ""))
                    .appendQueryParameter("lat", String.valueOf(latitude))
                    .appendQueryParameter("lon", String.valueOf(longitude))
                    .appendQueryParameter("lup", formattedDate);
            String myUrl1 = builder1.build().toString();
            sa.u = myUrl1;
            sa.execute();
        }

        // Toast.makeText(login.this, String.valueOf(latitude), Toast.LENGTH_LONG).show();

    }
    public void callAsynchronousTask1() {
        final Handler handler = new Handler();
        timer1 = new Timer();
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
        timer1.schedule(doAsynchronousTask, 0, 5000); //execute in every 50000 ms
    }
    static int ncheck=0;
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
        Intent notificationIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        int iconId = R.drawable.logo;
        int uniqueCode = new Random().nextInt(Integer.MAX_VALUE);
        Notification notification = new NotificationCompat.Builder(this)

                .setSmallIcon(iconId)
                .setContentTitle("MFE Vehicle Tracker")
                .setContentText("Location is not Enabled.. Click to go to Settings!")
                .setOngoing(true)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setLights(Color.RED,3000,3000)
                .setContentIntent(pendingIntent).build();

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        if(!gps_enabled && !network_enabled) {
            // notify user

          if(ncheck==0) {
              nm.notify(1, notification);
              ncheck = 1;
          }




           // testdialog.show();

        }
        else{
            nm.cancel(1);
            ncheck=0;
            //testdialog.dismiss();
        }
    }
    public void sendgpstohistory() {

        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate  = fmt.format(c);

                fmt = new SimpleDateFormat("HH:mm:ss");
        String formattedtm = fmt.format(c);


        //Toast.makeText(login.this, formattedDate, Toast.LENGTH_LONG).show();
        //String s =
        AsyncRetrieve2 sa = new AsyncRetrieve2();
        Uri.Builder builder1 = new Uri.Builder();
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        if(latitude >22 || longitude>22) {
            //inserthistory.php/?key=1345&lat=45&lon=55&username=ayesha222&dt=2018-09-04&tm=02:04:20
            builder1.scheme("https")
                    .authority("mfenter.com")

                    .appendPath("tracker")
                    .appendPath("inserthistory.php")
                    .appendQueryParameter("key", "1345")
                    .appendQueryParameter("username", login.sharedpreferences.getString("username", ""))
                    .appendQueryParameter("lat", String.valueOf(latitude))
                    .appendQueryParameter("lon", String.valueOf(longitude))
                    .appendQueryParameter("dt", formattedDate)
                    .appendQueryParameter("tm", formattedtm);
            String myUrl1 = builder1.build().toString();
            sa.u = myUrl1;
            sa.execute();
        }
        else {

           // Toast.makeText(MyService.this, "Please Enable the Location Service!", Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(login.this, String.valueOf(latitude), Toast.LENGTH_LONG).show();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //Log.w("MyService", "onBind callback called");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Log.w("MyService", "onStartCommand callback called");
        callAsynchronousTask1();
        callAsynchronousTask();
        showNotification();
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onCreate() {
        super.onCreate();
       // Log.w("MyService", "onCreate callback called");
    }

    @Override
    public void onDestroy() {
        ncheck=0;
          timer1.cancel();

        super.onDestroy();
       // Log.w("MyService", "onDestroy callback called");
    }

    public class AsyncRetrieve2 extends AsyncTask<String, String, String> {

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
}


package com.example.lilian.projetamio;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class WebService_Service extends Service {

    private String url;
    private Timer timer;
    private IntentFilter intentFilter;

    private double lastResult1 = 0;
    private double lastResult2 = 0;

    boolean light1 = false;
    boolean light2 = false;

    public static final String mBroadcastService = "actionInService";

    public WebService_Service() {
    }

    public void onCreate(){
        super.onCreate();
        timer = new Timer();
        intentFilter = new IntentFilter();
        intentFilter.addAction(mBroadcastService);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverService, intentFilter);
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        TimerTask task = new TimerTask(){
            public void run() {
                try {
                    AsyncEvent async = new AsyncEvent(getApplicationContext());
                    async.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }};

        timer.schedule(task,0,10000);

        return START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverService);
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private BroadcastReceiver broadcastReceiverService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(mBroadcastService)){

                double light1Value = intent.getDoubleExtra("DataLight1", -1);

                if (light1Value == -1) //Si valeur reçu est null
                {
                    if(light1Value - lastResult1 > 75 && light1Value > 250){
                        sendNotification(1,"Lumière 1 allumée");
                        light1 = true;
                    } else if(light1Value - lastResult1 < -75 || (light1Value < 250 && lastResult1 == 0)) {
                        sendNotification(1,"Lumière 1 éteinte");
                        light1 = false;
                    }
                    lastResult1 = light1Value;
                }

                double light2Value = intent.getDoubleExtra("DataLight2", -1);

                if (light2Value == -1){
                    if(light2Value - lastResult2 > 75  && light2Value > 250){
                        sendNotification(2,"Lumière 2 allumée");
                        light2 = true;
                    } else if(light2Value - lastResult2 < -75 || (light2Value < 250 && lastResult2 == 0)) {
                        sendNotification(2,"Lumière 2 éteinte");
                        light2 = false;
                    }
                    lastResult2 = light2Value;
                }

                Intent broadcastIntentActivity = new Intent(MainActivity.mBroadcastActivity);
                broadcastIntentActivity.putExtras(intent);
                broadcastIntentActivity.putExtra("light1", light1);
                broadcastIntentActivity.putExtra("light2", light2);
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntentActivity);
            }
        }
    };

    public void sendNotification(int ID,String message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("IoT")
                .setContentText(message)
                .setSmallIcon(R.drawable.notif1)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(ID,mBuilder.build());
    }

    public void sendEmail(String message,String recipient) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject of email");
        intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
        intent.setData(Uri.parse("mailto:default@recipient.com"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent,"Send mail ..."));
    }
}

package com.example.lilian.projetamio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class WebService_Service extends Service {

    private String url;
    private Timer timer;

    public WebService_Service() {
    }

    public void onCreate(){
        super.onCreate();
        timer = new Timer();
        Log.d("Service", "Creation service");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("Service", "Démarage service");
        TimerTask task = new TimerTask(){
            public void run() {
                Log.d("Timer", "Lancement" );
                try {
                    AsyncEvent async = new AsyncEvent(getApplicationContext());
                    Log.d("Timer", "sync" );
                    async.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }};

        timer.schedule(task,0,2000);

        return START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
        Log.d("Service", "Destruction du service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

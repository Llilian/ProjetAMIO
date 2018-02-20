package com.example.lilian.projetamio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.TimerTask;
import java.util.Timer;

public class MyService extends Service {


    int cpt;
    Timer timer;

    public MyService() {
    }

    public void onCreate(){
        super.onCreate();
        Log.d("Service", "Creation service");
        timer = new Timer();
        cpt = 0;
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("Service", "Démarage service");

        TimerTask task = new TimerTask(){
            public void run() {
                Log.d("Timer", "Compteur :" + cpt);
                cpt++;
            }};

        timer.schedule(task,0,1000);
        return START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();
        Log.d("Service", "Destruction du service");
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

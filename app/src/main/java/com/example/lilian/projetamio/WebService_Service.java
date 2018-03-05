package com.example.lilian.projetamio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;


public class WebService_Service extends Service {

    private String url;

    public WebService_Service() {
    }

    public void onCreate(){
        super.onCreate();
        Log.d("Service", "Creation service");
        url = "http://iotlab.telecomnancy.eu/rest/data/1/temperature/last";
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("Service", "Démarage service");
        GetDataIoT getData = new GetDataIoT();
        try {
            getData.downloadUrl(url);
        } catch(IOException ex){
            ex.getStackTrace();
        }


        return START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();
        Log.d("Service", "Destruction du service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

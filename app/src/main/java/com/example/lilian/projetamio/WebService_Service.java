package com.example.lilian.projetamio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;


public class WebService_Service extends Service {

    private String url;

    public WebService_Service() {
    }

    public void onCreate(){
        super.onCreate();
        Log.d("Service", "Creation service");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("Service", "Démarage service");
        Intent broadcastIntent = new Intent(MainActivity.mBroadcastAction);
        broadcastIntent.putExtra("Data", "value");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        try {
            AsyncEvent async = new AsyncEvent(getApplicationContext());
            async.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
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

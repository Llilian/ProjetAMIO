package com.example.lilian.projetamio;

import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lilian on 05/03/2018.
 */

public class AsyncEvent extends AsyncTask<String,String,List>{

    private Context context;
    MyParser myParser = new MyParser();
    List data_list;
    int Code = 0;

    public AsyncEvent(Context context) {
        this.context = context;
    }

    @Override
    protected List doInBackground(String... text){
        String result = null;
        List Array = new ArrayList();
        InputStream in = null;

        try {
            URL url = new URL("http://iotlab.telecomnancy.eu/rest/data/1/light1/last");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000); // en milliseconds
            urlConnection.setReadTimeout(500); // en milliseconds
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            urlConnection.connect();
            Code = urlConnection.getResponseCode();

            if (Code != HttpURLConnection.HTTP_OK) {
                return null;
            }
            else {
                in = urlConnection.getInputStream();
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            if (in != null) {
                data_list = myParser.readJsonStream(in);
                return data_list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... Progress)
    {

    }


    protected void onPostExecute(List listData) {

        Intent broadcastIntentService = new Intent(WebService_Service.mBroadcastService);

        for(int i = 0; i <= listData.size()-1; i++)
        {
            Data data = (Data)listData.get(i);
            Log.d("Resultat post exec","Mote : " + data.getMote() + " time : " + data.getTimestamp() + " light : " + data.getLightValue() + " label : " + data.getLabel());
            broadcastIntentService.putExtra("DataMote"+(i+1),  data.getMote());
            broadcastIntentService.putExtra("DataLight"+(i+1),  data.getLightValue());
            broadcastIntentService.putExtra("DataTime"+(i+1),  data.getTimestamp());
        }

        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntentService);
    }
}

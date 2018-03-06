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

public class AsyncEvent extends AsyncTask<String,String,String>{

    private Context context;
    MyParser myParser = new MyParser();
    List data_list;

    public AsyncEvent() {
    }

    @Override
    protected String doInBackground(String... text){
        String result = null;
        List Array = new ArrayList();
        InputStream in = null;
        int Code = 0;

        try {
            // Ouverture de la connexion
            URL url = new URL("http://iotlab.telecomnancy.eu/rest/data/1/light1/last");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000); // en milliseconds
            urlConnection.setReadTimeout(500); // en milliseconds
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            Code = urlConnection.getResponseCode();

            if (Code != HttpURLConnection.HTTP_OK) {
                Intent broadcastIntent = new Intent(MainActivity.mBroadcastActionError);
                broadcastIntent.putExtra("Data", "value");
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                //Return code au service, traitement dans celui ci
                //publishProgress("httpcode", String.valueOf(Code));
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

                Data lastData = (Data) data_list.get(data_list.size()-1);
                String lastResult = lastData.getLightValue() + " - timestamp : " + lastData.getTimestamp();
                Log.d("Datalist", "LightValue : " + lastResult);
                //return lastResult;
                //publishProgress("lastResult",lastResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    protected void onProgressUpdate(String... Progress)
    {
    }

    protected void onPostExecute(String result) {
        //textView.setText(result);
        Log.d("Result post exec","Downloaded : " + result + " octets");
    }
}

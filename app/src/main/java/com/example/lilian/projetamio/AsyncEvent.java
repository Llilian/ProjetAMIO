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
                //return "value return";
                //Return code au service, traitement dans celui ci
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
                return data_list;
                //publishProgress("lastResult",lastResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //TODO Toast ne s'affiche pas 1
    protected void onProgressUpdate(String... Progress)
    {
        if (Code != HttpURLConnection.HTTP_NOT_FOUND){
            Toast.makeText(context,"progress",Toast.LENGTH_LONG).show();
            Log.d("Publish","Downloaded : " );
        }
    }

    //TODO Toast ne s'affiche pas 2
    protected void onPostExecute(List listData) {
        //textView.setText(result);
        /*if (Code != HttpURLConnection.HTTP_NOT_FOUND){
            Toast.makeText(context,"post",Toast.LENGTH_LONG).show();
        }*/

        Intent broadcastIntent = new Intent(MainActivity.mBroadcastAction);

        for(int i = 0; i <= data_list.size()-1; i++)
        {
            Data data = (Data)listData.get(i);
            Log.d("Resultat post exec","Mote : " + data.getMote() + " time : " + data.getTimestamp() + " light : " + data.getLightValue() + " label : " + data.getLabel());
            broadcastIntent.putExtra("DataMote"+(i+1),  data.getMote());
            broadcastIntent.putExtra("DataLight"+(i+1),  data.getLightValue().toString());
            broadcastIntent.putExtra("DataTime"+(i+1),  data.getTimestamp());
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}

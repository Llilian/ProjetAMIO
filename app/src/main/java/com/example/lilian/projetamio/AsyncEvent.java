package com.example.lilian.projetamio;

import android.os.AsyncTask;
import android.content.Context;
import android.util.Log;

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

public class AsyncEvent extends AsyncTask {

    private Context context;
    MyParser myParser = new MyParser();
    List data_list;
    DataDownloadListener dataDownloadListener;

    public AsyncEvent() {
    }

    public void setDataDownloadListener(DataDownloadListener dataDownloadListener) {
        this.dataDownloadListener = dataDownloadListener;
    }

    protected String doInBackground(String... texte) {

        URL urls;
        String result = null;
        List Array = new ArrayList();
        String url = "http://iotlab.telecomnancy.eu/rest/data/1/light1/last";
        InputStream in = null;
        int Code = 0;

        try {
            // Ouverture de la connexion
            urls = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urls.openConnection();

            // Connexion à l'URL
            urlConnection.connect();
            Code = urlConnection.getResponseCode();

            if (Code != HttpURLConnection.HTTP_OK) {
                //publishProgress("httpcode", String.valueOf(Code));
            }
            else {
                in = urlConnection.getInputStream();
            }

        } catch (MalformedURLException e1) {

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





    protected void onProgressUpdate(String... Progress) {
        if(Progress != null)
        {
            dataDownloadListener.dataDownloadedSuccessfully(Progress);
        }
        else {
            dataDownloadListener.dataDownloadFailed();
        }
    }

    protected void onPostExecute(String result) {
        //textView.setText(result);
    }

    public static interface DataDownloadListener {
        void dataDownloadedSuccessfully(String... data);
        void dataDownloadFailed();
    }

}

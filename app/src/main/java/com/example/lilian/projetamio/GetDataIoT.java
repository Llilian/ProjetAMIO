package com.example.lilian.projetamio;

import android.accounts.NetworkErrorException;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by Lilian on 21/02/2018.
 */
public class GetDataIoT {

    public List downloadUrl(String url){
        InputStream inputStream = null;

        int codeReponse = 0;
        List dataList = null;

        try {
            URL urlTemp = new URL("http://www.android.com/");
            HttpURLConnection urlConnection = (HttpURLConnection) urlTemp.openConnection();
            urlConnection.setConnectTimeout(10000); // en milliseconds
            urlConnection.setReadTimeout(500); // en milliseconds
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            codeReponse = urlConnection.getResponseCode();

            if (codeReponse == HttpURLConnection.HTTP_OK)
                inputStream = urlConnection.getInputStream();
            Log.d("Connection Url", "Message : " + inputStream);

            dataList.add(codeReponse);

            Log.d("Connection Url", "Code réponse : " + codeReponse);
        } catch (MalformedURLException em) {
            Log.d("Erreur malformed url", "Erreur : " + em.getMessage());
        } catch(IOException e) {
            Log.d("Erreur connexion url", "Erreur : " + e.getMessage());
        }


        return dataList;
    }

}

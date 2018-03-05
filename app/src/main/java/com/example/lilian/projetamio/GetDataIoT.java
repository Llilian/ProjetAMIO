package com.example.lilian.projetamio;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Lilian on 21/02/2018.
 */
public class GetDataIoT {

    public List downloadUrl(String url) throws IOException{
        InputStream inputStream = null;

        int codeReponse = 0;
        List dataList = null;

        try {
            URL urlTemp = new URL(url);
            HttpURLConnection urlCon = (HttpURLConnection)urlTemp.openConnection();
            urlCon.setConnectTimeout(10000); // en milliseconds
            urlCon.setReadTimeout((500)); // en milliseconds
            urlCon.setRequestMethod("GET");
            urlCon.setDoInput(true);

            urlCon.connect();
            codeReponse = urlCon.getResponseCode();

            Log.d("Connection Url", "Message : " + codeReponse);
        }

    }

}

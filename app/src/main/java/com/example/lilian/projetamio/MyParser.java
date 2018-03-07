package com.example.lilian.projetamio;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lilian on 05/03/2018.
 */

public class MyParser {

    ArrayList data_list;

    public MyParser() {

        data_list = new ArrayList();

    }

    public List readJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginObject(); // On commence par l'object {data:[]}
            if (reader.hasNext()) {
                if (reader.nextName().equals("data")) {
                    reader.beginArray(); // la valeur de data est un array contenant plusieurs objects
                    while (reader.hasNext()) {
                        data_list.add(readData(reader));
                    }
                    reader.endArray();
                }
            }
            reader.endObject();
            return data_list;
        }
        catch (IOException e) {
            return null;
        }finally{
            reader.close();
        }
    }

    public Data readData(JsonReader reader) throws IOException {

        Long timestamp = null;
        String label = null;
        Double light_value = null;
        String mote = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("timestamp")){
                timestamp = reader.nextLong();
            }
            else if (name.equals("value")) {
                light_value = reader.nextDouble();
            }
            else if (name.equals("mote")) {
                mote = reader.nextString();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Data(timestamp,label,light_value,mote);
    }


    // Reads an InputStream and converts it to a String
    public String readIt(InputStream stream, int len) throws  IOException, UnsupportedOperationException {
        Reader reader = null;
        reader = new InputStreamReader(stream,"UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}

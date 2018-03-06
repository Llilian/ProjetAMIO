package com.example.lilian.projetamio;

import java.net.MalformedURLException;

/**
 * Created by Lilian on 05/03/2018.
 */

public class Data {
    private Long timestamp = null;
    private String label = null;
    private Double light_value = null;
    private String mote = null;

    public Data(Long timestamp, String label, Double light_value, String mote) throws MalformedURLException {
        this.timestamp = timestamp;
        this.label = label;
        this.light_value = light_value;
        this.mote = mote;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public String getLabel() {
        return this.label;
    }

    public  Double getLightValue() {
        return this.light_value;
    }

    public String getMote() {
        return this.mote;
    }
}

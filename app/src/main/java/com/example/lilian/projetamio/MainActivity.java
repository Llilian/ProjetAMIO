package com.example.lilian.projetamio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.health.TimerStat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.FormatFlagsConversionMismatchException;

public class MainActivity extends AppCompatActivity {

    private TextView tVServiceStatus;
    private TextView tVMote1;
    private TextView tVLight1;
    private TextView tVTime1;
    private TextView tVMote2;
    private TextView tVLight2;
    private TextView tVTime2;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    public static final String mBroadcastAction = "displayValue";
    public static final String mBroadcastActionError = "popupError";
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Main", "Création de l'activité");

        intentFilter = new IntentFilter();
        intentFilter.addAction(mBroadcastAction);

        // Initialisation TextView
        tVServiceStatus = findViewById(R.id.TVServiceStatus);
        tVMote1 = findViewById(R.id.TVMote1);
        tVLight1 = findViewById(R.id.TVLight1);
        tVTime1 = findViewById(R.id.TVTime1);
        tVMote2 = findViewById(R.id.TVMote2);
        tVLight2 = findViewById(R.id.TVLight2);
        tVTime2 = findViewById(R.id.TVTime2);

        // Code ToggleButton
        ToggleButton tb1 = (ToggleButton)findViewById(R.id.TglBtn1);
        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) // Service en cours -> Appuie pour l'arréter
                {
                    Log.d("Main", "Démmarage du service");
                    tVServiceStatus.setText("En cours");
                    startService(new Intent(getApplicationContext(), WebService_Service.class));
                }
                else // Service arrété -> Appuie pour le démarrer
                {
                    Log.d("Main","Arret du service");
                    tVServiceStatus.setText("Arrêté");
                    stopService(new Intent(getApplicationContext(), WebService_Service.class));
                }
            }
        });

        // Création d'une préférence boot à false par défaut
        prefs = getPreferences(Context.MODE_PRIVATE);
        edit = prefs.edit();
        edit.putBoolean("boot",false);
        edit.commit();

        // Code checkbox
        CheckBox cb = (CheckBox)findViewById(R.id.CheckBox1);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("Main", "CheckBox status changed");
                edit.putBoolean("boot",b);
                edit.commit();
            }
        });



        prefs.registerOnSharedPreferenceChangeListener(
                new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.d("Pref", "Changement de la pref");
            }
        });
    }

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastReceiver", "Recu");
            if(intent.getAction().equals(mBroadcastAction)){
                tVMote1.setText(intent.getStringExtra("DataMote1"));
                tVLight1.setText(intent.getStringExtra("DataLight1"));
                tVTime1.setText(convertTime(intent.getLongExtra("DataTime1", 0)));

                tVMote2.setText(intent.getStringExtra("DataMote2"));
                tVLight2.setText(intent.getStringExtra("DataLight2"));
                tVTime2.setText(convertTime(intent.getLongExtra("DataTime2", 0)));
            }

            else if(intent.getAction().equals(mBroadcastActionError))
                Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
        }
    };

    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.d("Main","Fin de l'activité");
    }

    private String convertTime(Long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }

}

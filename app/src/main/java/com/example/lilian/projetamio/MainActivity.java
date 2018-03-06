package com.example.lilian.projetamio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private TextView tV2;
    private TextView tV4;
    private TextView tV6;
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

        // Code ToggleButton
        ToggleButton tb1 = (ToggleButton)findViewById(R.id.TglBtn1);
        tV2 = findViewById(R.id.TV2);
        tV4 = findViewById(R.id.TV4);
        tV6 = findViewById(R.id.TV6);
        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) // Service en cours -> Appuie pour l'arréter
                {
                    Log.d("Main", "Démmarage du service");
                    tV2.setText("En cours");
                    startService(new Intent(getApplicationContext(), WebService_Service.class));
                }
                else // Service arrété -> Appuie pour le démarrer
                {
                    Log.d("Main","Arret du service");
                    tV2.setText("Arrêté");
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
                tV4.setText(intent.getStringExtra("DataLight"));
                tV6.setText(intent.getStringExtra("DataTime"));
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

}

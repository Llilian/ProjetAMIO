package com.example.lilian.projetamio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    TextView tV2;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Main", "Création de l'activité");

        // Code ToggleButton
        ToggleButton tb1 = (ToggleButton)findViewById(R.id.TglBtn1);
        tV2 = (TextView)findViewById(R.id.TV2);
        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) // Service en cours -> Appuie pour l'arréter
                {
                    Log.d("Main", "Démmarage du service");
                    tV2.setText("En cours");
                    startService(new Intent(getApplicationContext(), MyService.class));
                }
                else // Service arrété -> Appuie pour le démarrer
                {
                    Log.d("Main","Arret du service");
                    tV2.setText("Arrêté");
                    stopService(new Intent(getApplicationContext(), MyService.class));
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

    protected void onDestroy(){
        super.onDestroy();
        Log.d("Main","Fin de l'activité");
    }

}

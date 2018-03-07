package com.example.lilian.projetamio;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.health.TimerStat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
    private TextView tVGlobal;
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
        //tVGlobal = findViewById(R.id.TVGlobal);

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
                double light1Value = intent.getDoubleExtra("DataLight1", 0);
                tVLight1.setText(String.valueOf(light1Value));
                tVTime1.setText(convertTime(intent.getLongExtra("DataTime1", 0)));

                if(light1Value != 0 && light1Value > 250){
                    //lumière 1 allumé
                }

                tVMote2.setText(intent.getStringExtra("DataMote2"));
                double light2Value = intent.getDoubleExtra("DataLight2", 0);
                tVLight2.setText(String.valueOf(light2Value));
                tVTime2.setText(convertTime(intent.getLongExtra("DataTime2", 0)));

                if(light2Value != 0 && light2Value > 250){
                    //lumière 2 allumé
                } else {

                }

                /*tVGlobal.setText("\t\t\tMote : " + intent.getStringExtra("DataMote1") + ""
                        + "Date :\t" + convertTime(intent.getLongExtra("DataTime1", 0))
                        + "Luminosité :\t" + String.valueOf(light2Value)
                        + "\n\n");*/
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
/*
    public void sendNotification(String message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("IoT")
                .setContentText(message)
                //.setSmallIcon(R.drawable."image");
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1,mBuilder.build());
    }

    public void sendEmail(String message,String recipient) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject of email");
        intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
        intent.setData(Uri.parse("mailto:default@recipient.com"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
*/
}

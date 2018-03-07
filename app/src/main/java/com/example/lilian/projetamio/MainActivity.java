package com.example.lilian.projetamio;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView tVServiceStatus;
    private TextView tVMote1;
    private TextView tVLight1;
    private TextView tVTime1;
    private TextView tVLightStatus1;
    private TextView tVMote2;
    private TextView tVLight2;
    private TextView tVTime2;
    private TextView tVLightStatus2;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    private double lastResult1 = 0;
    private double lastResult2 = 0;

    public static final String mBroadcastActivity = "displayValue";
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter();
        intentFilter.addAction(mBroadcastActivity);

        // Initialisation TextView
        tVServiceStatus = findViewById(R.id.TVServiceStatus);
        tVMote1 = findViewById(R.id.TVMote1);
        tVLight1 = findViewById(R.id.TVLight1);
        tVTime1 = findViewById(R.id.TVTime1);
        tVMote2 = findViewById(R.id.TVMote2);
        tVLight2 = findViewById(R.id.TVLight2);
        tVTime2 = findViewById(R.id.TVTime2);
        tVLightStatus1 = findViewById(R.id.TVLightStatus1);
        tVLightStatus2 = findViewById(R.id.TVLightStatus2);

        // Code ToggleButton
        ToggleButton tb1 = (ToggleButton)findViewById(R.id.TglBtn1);
        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) // Service en cours -> Appuie pour l'arréter
                {
                    tVServiceStatus.setText("En cours");
                    startService(new Intent(getApplicationContext(), WebService_Service.class));
                }
                else // Service arrété -> Appuie pour le démarrer
                {
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
                edit.putBoolean("boot",b);
                edit.commit();
            }
        });



        prefs.registerOnSharedPreferenceChangeListener(
                new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
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
            /**
             * Mise à jour des valeurs affichées
             */
            if(intent.getAction().equals(mBroadcastActivity)){
                tVMote1.setText(intent.getStringExtra("DataMote1"));
                double light1Value = intent.getDoubleExtra("DataLight1", -1);
                tVLight1.setText(String.valueOf(light1Value));
                tVTime1.setText(convertTime(intent.getLongExtra("DataTime1", 0)));
                if(intent.getBooleanExtra("light1",false))
                    tVLightStatus1.setText("Allumée");
                else
                    tVLightStatus1.setText("Eteinte");


                tVMote2.setText(intent.getStringExtra("DataMote2"));
                double light2Value = intent.getDoubleExtra("DataLight2", -1);
                tVLight2.setText(String.valueOf(light2Value));
                tVTime2.setText(convertTime(intent.getLongExtra("DataTime2", 0)));
                if(intent.getBooleanExtra("light2",false))
                    tVLightStatus2.setText("Allumée");
                else
                    tVLightStatus2.setText("Eteinte");
            }
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
    public void sendNotification(int ID,String message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("IoT")
                .setContentText(message)
                .setSmallIcon(R.drawable.notif1)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(ID,mBuilder.build());
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

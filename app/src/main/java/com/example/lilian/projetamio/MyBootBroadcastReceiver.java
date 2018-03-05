package com.example.lilian.projetamio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startService = new Intent(context, MyService.class);
        context.startService(startService);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

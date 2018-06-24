package zhao.dong.bucketdrops.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import zhao.dong.bucketdrops.extras.Util;

public class BootReceiver extends BroadcastReceiver {

    public BootReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Util.scheduleAlarm(context);
    }
}
package reservation.quandoo.com.quandooreservation.maintenance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Broadcast receiver to be started by AlarmManager
 */
public class MyAlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "MyAlarmReceiver";
    public static final int REQUEST_CODE = 1234;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        Log.d(TAG, "onReceive starting service");

        Intent serviceIntent = CleanupIntentService.getServiceIntent(context);
        context.startService(serviceIntent);
    }
}

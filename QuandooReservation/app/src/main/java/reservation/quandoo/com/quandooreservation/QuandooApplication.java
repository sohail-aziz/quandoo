package reservation.quandoo.com.quandooreservation;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import reservation.quandoo.com.quandooreservation.di.ApplicationComponent;
import reservation.quandoo.com.quandooreservation.di.ApplicationModule;
import reservation.quandoo.com.quandooreservation.di.DaggerApplicationComponent;
import reservation.quandoo.com.quandooreservation.maintenance.MyAlarmReceiver;


/**
 * Application class initializing dagger and scheduling alarm
 * <p>
 * Created by sohailaziz on 16/12/17.
 */

public class QuandooApplication extends Application {

    private ApplicationComponent mComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        initComponent(this);

        scheduleAlarm();
    }

    private void scheduleAlarm() {

        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext()
                , MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        long interval = 1000 * 60 * 15;//15 min

        long firstTriggerTime = System.currentTimeMillis() + interval;


        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstTriggerTime, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }

    private void initComponent(Context context) {
        mComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .build();
    }


}

package reservation.quandoo.com.quandooreservation.maintenance;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import reservation.quandoo.com.quandooreservation.QuandooApplication;
import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.data.local.Table;
import reservation.quandoo.com.quandooreservation.data.local.TableDao;

/**
 * A database clean up intent service to be started by receiver
 */
public class CleanupIntentService extends IntentService {

    public static final String TAG = "CleanupIntentService";

    @Inject
    Repository repository;

    public static Intent getServiceIntent(Context context) {

        Intent intent = new Intent(context, CleanupIntentService.class);
        return intent;
    }

    public CleanupIntentService() {
        super("CleanupIntentService");
        Log.d(TAG, "CleanupIntentService");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        ((QuandooApplication) getApplication()).getComponent().inject(this);
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent");

        Log.d(TAG, "reseting all tables ...");
        repository.resetAllTables();

    }


}

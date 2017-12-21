package reservation.quandoo.com.quandooreservation;

import android.app.Application;
import android.content.Context;

import reservation.quandoo.com.quandooreservation.di.ApplicationComponent;
import reservation.quandoo.com.quandooreservation.di.ApplicationModule;
import reservation.quandoo.com.quandooreservation.di.DaggerApplicationComponent;


/**
 * Created by sohailaziz on 16/12/17.
 */

public class QuandooApplication extends Application {

    private  ApplicationComponent mComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        initComponent(this);
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }

    private  void initComponent(Context context) {
        mComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .build();
    }


}

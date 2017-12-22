package reservation.quandoo.com.quandooreservation.di;

import javax.inject.Singleton;

import dagger.Component;
import reservation.quandoo.com.quandooreservation.maintenance.CleanupIntentService;
import reservation.quandoo.com.quandooreservation.presentation.view.MainActivity;
import reservation.quandoo.com.quandooreservation.presentation.view.TableActivity;

/**
 * Dagger application component defining injection points
 *
 * Created by sohailaziz on 16/12/17.
 */


@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {


    void inject(MainActivity activity);

    void inject(TableActivity activity);

    void inject(CleanupIntentService cleanupIntentService);

}
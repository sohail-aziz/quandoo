package reservation.quandoo.com.quandooreservation.di;

import javax.inject.Singleton;

import dagger.Component;
import reservation.quandoo.com.quandooreservation.presentation.MainActivity;

/**
 * Dagger application component defining injection points
 *
 * Created by sohailaziz on 16/12/17.
 */


@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {


    void inject(MainActivity activity);



}
package reservation.quandoo.com.quandooreservation.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import reservation.quandoo.com.quandooreservation.Constants;
import reservation.quandoo.com.quandooreservation.data.QuandooAPI;
import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.data.RepositoryImpl;
import reservation.quandoo.com.quandooreservation.data.local.CustomerDao;
import reservation.quandoo.com.quandooreservation.data.local.QuandooDatabase;
import reservation.quandoo.com.quandooreservation.data.local.TableDao;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module provides Application wide dependencies
 * <p>
 * Created by sohailaziz on 15/12/17.
 */

@Module
public class ApplicationModule {


    private final Context context;

    private static final String NAME_BASE_URL = "NAME_BASE_URL";

    @Provides
    @Named(NAME_BASE_URL)
    String provideBaseUrlString() {
        return Constants.BASE_URL;
    }

    public ApplicationModule(Context context) {
        this.context = context.getApplicationContext();
    }


    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.context;
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {

        return new OkHttpClient.Builder().build();

    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, @Named(NAME_BASE_URL) String baseUrl) {

        Gson gson = new Gson();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }



    @Provides
    @Singleton
    QuandooAPI provideAPI(Retrofit retrofit) {
        return retrofit.create(QuandooAPI.class);
    }

    @Provides
    @Singleton
    Repository provideRepository(RepositoryImpl repository) {
        return repository;
    }


    @Provides
    @Singleton
    QuandooDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), QuandooDatabase.class, "app_database").build();
    }

    @Singleton
    @Provides
    CustomerDao provideCustomerDao(QuandooDatabase database) {
        return database.customerDao();
    }

    @Provides
    @Singleton
    TableDao provideTableDao(QuandooDatabase database) {
        return database.tableDao();
    }


}

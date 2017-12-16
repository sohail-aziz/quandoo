package reservation.quandoo.com.quandooreservation.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import reservation.quandoo.com.quandooreservation.Constants;
import reservation.quandoo.com.quandooreservation.data.QuandooAPI;
import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.data.RepositoryImpl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module provides Application wide dependencies
 *
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

        HttpLoggingInterceptor.Level LOG_LEVEL = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(LOG_LEVEL);

        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
//        return new OkHttpClient.Builder().build();

    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, @Named(NAME_BASE_URL) String baseUrl) {

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory()).create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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


}

package reservation.quandoo.com.quandooreservation.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import reservation.quandoo.com.quandooreservation.QuandooApplication;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.data.response.Customer;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Inject
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        injectDependencies();

        loadCustomers();
        loadTables();
    }

    private void loadTables() {
        Log.d(TAG, "loadTables");

        Observable<List<Boolean>> observable= repository.getTables();

        Subscription subscription= observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Boolean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Boolean> tables) {

                        Log.d(TAG, "tables=" + tables);
                    }
                });
    }

    private void loadCustomers() {
        Log.d(TAG, "loadCustomers");


       Observable<List<Customer>> observable=repository.getCustomers();

        Subscription subscription=observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Customer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Customer> customers) {

                        Log.d(TAG, "customers=" + customers);
                    }
                })
        ;
    }

    private void injectDependencies() {

        ((QuandooApplication)getApplication()).getComponent().inject(this);

    }
}

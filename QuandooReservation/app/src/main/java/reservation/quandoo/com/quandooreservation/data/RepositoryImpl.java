package reservation.quandoo.com.quandooreservation.data;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import reservation.quandoo.com.quandooreservation.data.local.CustomerDao;


/**
 * Created by sohailaziz on 16/12/17.
 */

public class RepositoryImpl implements Repository {

    public static final String TAG = "RepositoryImpl";

    private QuandooAPI quandooAPI;
    private final CustomerDao customerDao;


    @Inject
    public RepositoryImpl(QuandooAPI quandooAPI, Context context, CustomerDao customerDao) {
        this.quandooAPI = quandooAPI;
        this.customerDao = customerDao;
    }


    @Override
    public Observable<List<Customer>> getCustomers() {
        Log.d(TAG, "getCustomers");
        return Observable.concatArray(
                getCustomerFromDb(),
                getCustomerFromApi());

    }

    private Observable<List<Customer>> getCustomerFromDb() {
        Log.d(TAG, "getCustomerFromDb");

        return this.customerDao.getCustomers()
                .filter(new Predicate<List<Customer>>() {
                    @Override
                    public boolean test(@NonNull List<Customer> customers) throws Exception {
                        return !customers.isEmpty();
                    }
                })
                .toObservable();

    }

    private Observable<List<Customer>> getCustomerFromApi() {
        Log.d(TAG, "getCustomerFromAPI");
        return quandooAPI.getCustomers()
                .doOnNext(new Consumer<List<Customer>>() {
                    @Override
                    public void accept(@NonNull List<Customer> customerEntities) throws Exception {
                        Log.d(TAG, "Do onNext, calling storeCustomerInDb");

                        storeCustomerInDb(customerEntities);
                    }
                });
    }

    private void storeCustomerInDb(final List<Customer> customerEntities) {
        Log.d(TAG, "storeCustomerInDb");

        Observable.fromCallable(new Callable<List<Customer>>() {
            @Override
            public List<Customer> call() throws Exception {

                Log.d(TAG, "calling customerDao.addAllCustomers");
                customerDao.addAllCustomers(customerEntities);
                return customerEntities;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<Customer>>() {
                    @Override
                    public void accept(@NonNull List<Customer> o) throws Exception {
                        Log.d(TAG, "total stored customers in db=" + o.size());
                    }
                });
    }

    @Override
    public Observable<List<Boolean>> getTables() {
        return quandooAPI.getTables();
    }
}

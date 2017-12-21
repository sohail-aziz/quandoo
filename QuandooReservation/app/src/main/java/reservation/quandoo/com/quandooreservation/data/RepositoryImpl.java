package reservation.quandoo.com.quandooreservation.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import reservation.quandoo.com.quandooreservation.data.local.CustomerDao;
import reservation.quandoo.com.quandooreservation.data.local.Table;
import reservation.quandoo.com.quandooreservation.data.local.TableDao;


/**
 * Created by sohailaziz on 16/12/17.
 */

public class RepositoryImpl implements Repository {

    public static final String TAG = "RepositoryImpl";

    private final QuandooAPI quandooAPI;
    private final CustomerDao customerDao;
    private final TableDao tableDao;


    @Inject
    public RepositoryImpl(QuandooAPI quandooAPI, CustomerDao customerDao, TableDao tableDao) {
        this.quandooAPI = quandooAPI;
        this.customerDao = customerDao;
        this.tableDao = tableDao;
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
    public Observable<List<Table>> getTables() {

        Log.d(TAG, "getTables");
        return Observable.concatArray(
                getTablesFromDb(),
                getTablesFromApi());

    }

    private Observable<List<Table>> getTablesFromDb() {
        Log.d(TAG, "getTablesFromDb");

        return tableDao.getTables().filter(new Predicate<List<Table>>() {
            @Override
            public boolean test(@NonNull List<Table> tables) throws Exception {

                return !tables.isEmpty();
            }
        }).toObservable();
    }

    private Observable<List<Table>> getTablesFromApi() {

        return quandooAPI.getTables().map(new Function<List<Boolean>, List<Table>>() {
            @Override
            public List<Table> apply(@NonNull List<Boolean> input) throws Exception {
                return mapToTable(input);
            }
        }).doOnNext(new Consumer<List<Table>>() {
            @Override
            public void accept(@NonNull List<Table> tables) throws Exception {

                storeTableInDb(tables);
            }
        });
    }


    private void storeTableInDb(final List<Table> tableEntities) {

        Log.d(TAG, "storeTableInDb");

        Observable.fromCallable(new Callable<List<Table>>() {
            @Override
            public List<Table> call() throws Exception {

                tableDao.addTables(tableEntities);
                return tableEntities;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<Table>>() {
                    @Override
                    public void accept(@NonNull List<Table> tables) throws Exception {
                        Log.d(TAG, "total tables stored in db=" + tables.size());

                    }
                });
    }


    @Override
    public void udpateTable(Table table) {

    }

    private List<Table> mapToTable(List<Boolean> inputList) {
        List<Table> tableList = new ArrayList<>(inputList.size());
        int id = 0;
        for (Boolean b : inputList) {
            tableList.add(new Table(id, b));
            ++id;
        }
        return tableList;
    }
}

package reservation.quandoo.com.quandooreservation.data;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

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
 * Repository Implementation provides data from different sources API and databse
 * <p>
 * Created by sohailaziz on 16/12/17.
 */

@Singleton
public class RepositoryImpl implements Repository {

    public static final String TAG = "RepositoryImpl";

    private final QuandooAPI quandooAPI;
    private final CustomerDao customerDao;
    private final TableDao tableDao;
    private final TableMapper tableMapper;


    @Inject
    public RepositoryImpl(QuandooAPI quandooAPI, CustomerDao customerDao, TableDao tableDao, TableMapper tableMapper) {
        this.quandooAPI = quandooAPI;
        this.customerDao = customerDao;
        this.tableDao = tableDao;
        this.tableMapper = tableMapper;
    }


    /**
     * Fetches customers list from database and API serially
     *
     * @return Observable {@link List<Customer>}
     */
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


    /**
     * Fetches list of tables from database and API serially
     * updates local database with API results
     *
     * @return {@link List<Table>}
     */
    @Override
    public Observable<List<Table>> getTables() {

        Log.d(TAG, "getTables");
        return Observable.concatArray(
                getTablesFromDb(),
                getTablesFromApi());

    }

    /**
     * Updates table to local database
     *
     * @param table {@link Table}
     * @return
     */
    @Override
    public Observable<Table> updateTable(final Table table) {

        return Observable.fromCallable(new Callable<Table>() {
            @Override
            public Table call() throws Exception {
                tableDao.updateTable(table);
                return table;
            }
        });


    }

    private Observable<List<Table>> getTablesFromDb() {
        Log.d(TAG, "getTablesFromDb");

        Observable<List<Table>> observable = tableDao.getTables().filter(new Predicate<List<Table>>() {
            @Override
            public boolean test(@NonNull List<Table> tables) throws Exception {

                return !tables.isEmpty();
            }
        }).toObservable().doOnNext(new Consumer<List<Table>>() {
            @Override
            public void accept(@NonNull List<Table> tables) throws Exception {
                Log.d(TAG, "fetching table from db... tables size=" + tables.size());

            }
        });

        return observable;
    }

    private Observable<List<Table>> getTablesFromApi() {

        return quandooAPI.getTables().map(new Function<List<Boolean>, List<Table>>() {
            @Override
            public List<Table> apply(@NonNull List<Boolean> input) throws Exception {
                return tableMapper.map(input);
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


    /**
     * Resets all table's availability to true in local database asynchronously
     */
    @Override
    public void resetAllTables() {
        Log.d(TAG, "resetAllTables");
        Observable<List<Table>> observable = tableDao.getTables().toObservable();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<Table>>() {
                    @Override
                    public void accept(@NonNull List<Table> tables) throws Exception {

                        Log.d(TAG, "accept: updating tables");
                        for (Table t : tables) {
                            t.setAvailable(true);
                        }

                        tableDao.updateAllTables(tables);
                    }
                });

    }


}

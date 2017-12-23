package reservation.quandoo.com.quandooreservation.presentation.presenter;

import android.support.v4.util.Preconditions;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import reservation.quandoo.com.quandooreservation.ErrorMessageFactory;
import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import reservation.quandoo.com.quandooreservation.data.local.Table;
import reservation.quandoo.com.quandooreservation.presentation.view.BaseView;


/**
 * Table Presenter in MVP
 * Fetches get/set data from Repository and notifies view
 * <p>
 * Created by sohailaziz on 16/12/17.
 */

public class TablePresenter {

    public interface TablesView extends BaseView {

        void onTableDataLoaded(List<Table> tablesStates);

        void onTableDataError(String errorMessage);

        void onTableBooked(Table table);

        void onTableBookError(String errorMessage);
    }

    public static final String TAG = "TablePresenter";
    private final Repository repository;
    private final ErrorMessageFactory errorMessageFactory;
    private TablesView view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public TablePresenter(Repository repository, ErrorMessageFactory errorMessageFactory) {
        this.repository = repository;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(TablesView view) {
        this.view = view;
    }

    public void getTablesData() {

        if (this.view == null) {
            throw new IllegalStateException("view not set");
        }


        showProgress();

        Observable<List<Table>> observable = repository.getTables();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(new Observer<List<Table>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                        Log.d(TAG, "onError: e=" + e.getMessage());
                        hideProgress();
                        view.onTableDataError(errorMessageFactory.getErrorMessage(e));

                    }

                    @Override
                    public void onNext(List<Table> tables) {
                        Log.d(TAG, "onNext: tables size=" + tables.size());
                        hideProgress();
                        view.onTableDataLoaded(tables);
                    }
                });
    }

    /**
     * Updates table availability
     *
     * @param table
     * @param customer
     */
    public void bookTable(final Table table, Customer customer) {

        if (table == null || customer == null) {
            throw new IllegalArgumentException("table or customer cannot be null");
        }
        //set availability false
        table.setAvailable(false);

        Observable<Table> observable = repository.updateTable(table);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Table>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "bookTable: onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Table table) {
                        Log.d(TAG, "bookTable: onNext");
                        view.onTableBooked(table);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "bookTable: onError");

                        hideProgress();
                        view.onTableBookError(errorMessageFactory.getErrorMessage(e));

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "bookTable: onComplete");
                        hideProgress();

                    }
                });


    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    private void hideProgress() {
        this.view.hideLoading();
    }

    private void showProgress() {
        this.view.showLoading();
    }


}

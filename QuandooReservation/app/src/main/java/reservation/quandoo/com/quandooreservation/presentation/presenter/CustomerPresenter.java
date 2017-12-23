package reservation.quandoo.com.quandooreservation.presentation.presenter;

import android.util.Log;

import java.io.IOException;
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
import reservation.quandoo.com.quandooreservation.presentation.view.BaseView;


/**
 * Customer Presenter in MVP
 * Fetches data from Repository and notifies view
 * <p>
 * Created by sohailaziz on 16/12/17.
 */


public class CustomerPresenter {

    public interface CustomerView extends BaseView {

        void onCustomersLoaded(List<Customer> customers);

        void onCustomersError(String errorMessage);
    }


    public static final String TAG = "CustomerPresenter";
    private CustomerView view;
    private final Repository repository;
    private final ErrorMessageFactory errorMessageFactory;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public CustomerPresenter(Repository repository, ErrorMessageFactory errorMessageFactory) {
        this.repository = repository;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(CustomerView view) {
        this.view = view;
    }

    public void onDestroy() {

        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }

    }

    public void getCustomers() {

        if (this.view == null) {
            throw new IllegalStateException("View not set");
        }


        showProgress();

        Observable<List<Customer>> observable = repository.getCustomers();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(new Observer<List<Customer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                        Log.d(TAG, "onSubscribe()");
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

                        view.onCustomersError(errorMessageFactory.getErrorMessage(e));

                    }

                    @Override
                    public void onNext(List<Customer> customers) {
                        Log.d(TAG, "onNext customer size=" + customers.size());
                        hideProgress();
                        view.onCustomersLoaded(customers);

                    }
                });


    }

    private void showProgress() {

        view.showLoading();
    }

    private void hideProgress() {
        view.hideLoading();
    }
}

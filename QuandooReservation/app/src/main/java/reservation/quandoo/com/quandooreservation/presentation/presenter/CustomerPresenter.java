package reservation.quandoo.com.quandooreservation.presentation.presenter;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.data.response.Customer;
import reservation.quandoo.com.quandooreservation.presentation.view.BaseView;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  Presenter in MVP
 *
 * Created by sohailaziz on 16/12/17.
 */

public class CustomerPresenter {

    public interface CustomerView extends BaseView {

        void onCustomersLoaded(List<Customer> customers);

        void onCustomersError(String errorMessage);
    }


    public static final String TAG = "CustomerPresenter";
    private  CustomerView view;
    private final Repository repository;

    private Subscription subscription;


    @Inject
    public CustomerPresenter(Repository repository) {
        this.repository = repository;

    }

    public void setView(CustomerView view) {
        this.view=view;
    }

    public void onDestroy() {

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    public void getCustomers() {

        if (this.view == null) {
            throw new IllegalStateException("View not set");
        }

        showProgress();

        Observable<List<Customer>> observable=repository.getCustomers();

        subscription=observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Customer>>() {
                    @Override
                    public void onCompleted() {

                        hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        view.onCustomersError(e.getMessage());

                    }

                    @Override
                    public void onNext(List<Customer> customers) {

                        Log.d(TAG, "customers=" + customers);
                        view.onCustomersLoaded(customers);

                    }
                })
                ;
    }

    private void showProgress() {

        view.showLoading();
    }

    private void hideProgress() {
        view.hideLoading();
    }
}

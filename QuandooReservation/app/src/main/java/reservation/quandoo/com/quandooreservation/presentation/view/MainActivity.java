package reservation.quandoo.com.quandooreservation.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import reservation.quandoo.com.quandooreservation.QuandooApplication;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import reservation.quandoo.com.quandooreservation.presentation.presenter.CustomerPresenter;

public class MainActivity extends BaseActivity implements CustomerPresenter.CustomerView {

    private static final String TAG = "MainActivity";

    @Inject
    CustomerPresenter presenter;

    @Bind(R.id.progress_loading)
    ProgressBar progressBarLoading;

    @Bind(R.id.recycler_view_customers)
    RecyclerView recyclerViewCustomers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        injectDependencies();
        presenter.setView(this);
        loadCustomers();

    }


    private void loadCustomers() {
        Log.d(TAG, "loadCustomers");
        presenter.getCustomers();
    }

    private void injectDependencies() {
        ((QuandooApplication) getApplication()).getComponent().inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        progressBarLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBarLoading.setVisibility(View.GONE);
    }

    @Override
    public void onCustomersLoaded(List<Customer> customers) {

        Log.d(TAG, "onCustomersLoaded: customers size=" + customers.size());

        CustomerAdapter adapter = new CustomerAdapter(this, new CustomerAdapter.OnCustomerClickListener() {
            @Override
            public void onCustomerClicked(Customer customer) {

                Intent tableActivity = TableActivity.getCallingIntent(MainActivity.this, customer);
                startActivity(tableActivity);

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCustomers.setLayoutManager(linearLayoutManager);
        recyclerViewCustomers.setAdapter(adapter);

        adapter.updateData(customers);


    }

    @Override
    public void onCustomersError(String errorMessage) {
        showToast(errorMessage);
    }
}

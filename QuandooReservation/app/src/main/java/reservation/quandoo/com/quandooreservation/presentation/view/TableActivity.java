package reservation.quandoo.com.quandooreservation.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import reservation.quandoo.com.quandooreservation.QuandooApplication;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import reservation.quandoo.com.quandooreservation.data.local.Table;
import reservation.quandoo.com.quandooreservation.presentation.presenter.TablePresenter;

public class TableActivity extends BaseActivity implements TablePresenter.TablesView {

    public static final String KEY_CUSTOMER_EXTRA = "TableActivity.customer_extra";
    private static final String TAG = "TableActivity";

    @Bind(R.id.progress_loading)
    ProgressBar progressBarLoading;

    @Bind(R.id.recycler_view_tables)
    RecyclerView recyclerViewTables;

    @Bind(R.id.text_view_customer_name)
    TextView textViewCustomerName;

    @Inject
    TablePresenter presenter;

    private TableAdapter adapter;
    private Customer customer;
    private boolean alreadyBooked = false;

    public static Intent getCallingIntent(Context context, Customer customer) {
        Intent intent = new Intent(context, TableActivity.class);
        intent.putExtra(KEY_CUSTOMER_EXTRA, customer);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_table);
        ButterKnife.bind(this);
        initView();
        injectDependencies();

        presenter.setView(this);
        loadTables();


    }


    private void initView() {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_CUSTOMER_EXTRA)) {
            customer = intent.getParcelableExtra(KEY_CUSTOMER_EXTRA);
            textViewCustomerName.setText(customer.getCustomerFirstName() + " " + customer.getCustomerLastName());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    private void loadTables() {

        presenter.getTablesData();

    }

    private void injectDependencies() {
        ((QuandooApplication) getApplication()).getComponent().inject(this);
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
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }

    @Override
    public void onTableDataLoaded(List<Table> tablesStates) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        adapter = new TableAdapter(this, new TableAdapter.OnTableClickListener() {
            @Override
            public void onTableClick(Table table) {
                if (!alreadyBooked) {
                    presenter.bookTable(table, customer);
                } else {
                    showToast("Table for customer " + customer.getCustomerFirstName() + " has already been booked");
                }

            }
        });

        recyclerViewTables.setLayoutManager(gridLayoutManager);
        recyclerViewTables.setAdapter(adapter);

        adapter.updateAllTable(tablesStates);


    }

    @Override
    public void onTableDataError(String errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public void onTableBooked(Table table) {
        Log.d(TAG, "onTableBooked: tableId=" + table.getId());
        this.alreadyBooked = true;

        //TODO reloading will get tables (unchanged) from API, once updateTable API is available calling loadTables will show consistent state.
        //loadTables();

        //FIXME remove this once updateTable API is available
        adapter.updateTable(table);
        showToast("Table No " + table.getId() + " booked successfully");


    }

    @Override
    public void onTableBookError(String errorMessage) {

        showToast(errorMessage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}

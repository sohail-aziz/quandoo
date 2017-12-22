package reservation.quandoo.com.quandooreservation.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import reservation.quandoo.com.quandooreservation.QuandooApplication;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import reservation.quandoo.com.quandooreservation.data.local.Table;
import reservation.quandoo.com.quandooreservation.presentation.presenter.TablePresenter;

public class TableActivity extends AppCompatActivity implements TablePresenter.TablesView {

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
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Select Table for:")
                    .append("\n")
                    .append(customer.getCustomerFirstName())
                    .append(" ")
                    .append(customer.getCustomerLastName());

            textViewCustomerName.setText(stringBuilder.toString());
        }
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
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        presenter.onDestroy();
    }

    @Override
    public void onTableDataLoaded(List<Table> tablesStates) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        adapter = new TableAdapter(this, new TableAdapter.OnTableClickListener() {
            @Override
            public void onTableClick(Table table) {

                presenter.bookTable(table, customer);

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
        adapter.updateTable(table);
        Toast.makeText(this, "Table No " + table.getId() + " booked successfully", Toast.LENGTH_SHORT).show();


        //TODO reloading will get tables (unchanged) from API, once updateTable API is available,
        //TODO calling loadTables will show consistent state.
        //loadTables();

    }

    @Override
    public void onTableBookError(String errorMessage) {

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

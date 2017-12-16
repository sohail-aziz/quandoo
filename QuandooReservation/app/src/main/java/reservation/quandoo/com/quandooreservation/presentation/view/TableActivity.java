package reservation.quandoo.com.quandooreservation.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import reservation.quandoo.com.quandooreservation.QuandooApplication;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.response.Customer;
import reservation.quandoo.com.quandooreservation.presentation.presenter.TablePresenter;

public class TableActivity extends AppCompatActivity implements TablePresenter.TablesView{

    public static final String KEY_CUSTOMER_EXTRA = "TableActivity.customer_extra";
    @Bind(R.id.progress_loading)
    ProgressBar progressBarLoading;

    @Bind(R.id.recycler_view_tables)
    RecyclerView recyclerViewTables;

    @Inject
    TablePresenter presenter;

    public static Intent getCallingIntent(Context context, Customer customer) {
        Intent intent= new Intent(context, TableActivity.class);
        intent.putExtra(KEY_CUSTOMER_EXTRA, customer);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        ButterKnife.bind(this);
        injectDependencies();

        presenter.setView(this);

        loadTables();
    }

    private void loadTables() {

        presenter.getTablesData();

    }

    private void injectDependencies() {
        ((QuandooApplication)getApplication()).getComponent().inject(this);
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
    public void onTableDataLoaded(List<Boolean> tablesStates) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        TableAdapter adapter = new TableAdapter(this, tablesStates);

        recyclerViewTables.setLayoutManager(gridLayoutManager);
        recyclerViewTables.setAdapter(adapter);



    }

    @Override
    public void onTableDataError(String errorMessage) {

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}

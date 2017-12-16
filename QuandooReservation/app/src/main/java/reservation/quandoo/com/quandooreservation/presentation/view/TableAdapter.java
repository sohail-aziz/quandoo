package reservation.quandoo.com.quandooreservation.presentation.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.response.Customer;

/**
 * Created by sohailaziz on 16/12/17.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.CustomerViewHolder> {

    private Context context;
    private List<Boolean> tablesStates;

    public TableAdapter(Context context, List<Boolean> tablesStates) {

        this.context=context;
        this.tablesStates=tablesStates;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(context).inflate(R.layout.table_layout_row, parent, false);
        return new CustomerViewHolder(root);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {

        holder.checkBoxTable.setText(String.valueOf(position+1));
        holder.checkBoxTable.setChecked(tablesStates.get(position));

        holder.checkBoxTable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //TODO callback table state change
            }
        });

    }

    @Override
    public int getItemCount() {
        return tablesStates.size();
    }


    static class CustomerViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.cb_table_name)
        CheckBox checkBoxTable;
        public CustomerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

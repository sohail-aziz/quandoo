package reservation.quandoo.com.quandooreservation.presentation.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.local.Table;

/**
 * Created by sohailaziz on 16/12/17.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.CustomerViewHolder> {

    public interface OnTableClickListener{
        void onTableClick(Table selectedTable);
    }

    private Context context;
    private List<Table> tablesStates = new ArrayList<>();
    private OnTableClickListener tableClickListener;


    public TableAdapter(Context context, OnTableClickListener tableClickListener) {

        this.context=context;
        this.tableClickListener = tableClickListener;
    }

    public void updateAllTable(List<Table> tablesStates) {
        this.tablesStates.clear();
        this.tablesStates.addAll(tablesStates);
        notifyDataSetChanged();
    }

    public void updateTable(Table table) {

        tablesStates.get(table.getId()).setAvailable(false);
        notifyDataSetChanged();
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(context).inflate(R.layout.table_layout_row, parent, false);
        return new CustomerViewHolder(root);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, final int position) {

        holder.buttonTableName.setText(String.valueOf(position));
        Table table = tablesStates.get(position);
        if (table.isAvailable()) {
            holder.buttonTableName.setEnabled(true);
        } else {
            holder.buttonTableName.setEnabled(false);
        }

        holder.buttonTableName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableClickListener.onTableClick(tablesStates.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return tablesStates.size();
    }


    static class CustomerViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.button_table_name)
        Button buttonTableName;
        public CustomerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


    }
}

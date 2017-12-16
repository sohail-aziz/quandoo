package reservation.quandoo.com.quandooreservation.presentation.view;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.response.Customer;

/**
 * Created by sohailaziz on 16/12/17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    public interface OnCustomerClickListener{
        void onCustomerClicked(Customer customer);
    }

    private Context context;
    private List<Customer> customers=new ArrayList<>();
    private OnCustomerClickListener customerClickListener;

    public CustomerAdapter(Context context, OnCustomerClickListener customerClickListener) {
        this.context=context;
        this.customerClickListener=customerClickListener;

    }

    public void updateData(List<Customer> customers) {
        this.customers.clear();
        this.customers.addAll(customers);
        notifyDataSetChanged();
    }


    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(context).inflate(R.layout.customer_layout_row, parent, false);
        return new CustomerViewHolder(root,customerClickListener);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {

        holder.bind(customers.get(position), customerClickListener);

    }

    @Override
    public int getItemCount() {
        return customers.size();
    }


    static class CustomerViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.text_view_customer_name)
        TextView textViewName;
        public CustomerViewHolder(View itemView,OnCustomerClickListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(final Customer customer, final OnCustomerClickListener customerClickListener) {

            textViewName.setText(customer.firstName() + " " + customer.lastName());
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customerClickListener.onCustomerClicked(customer);

                }
            });
        }
    }
}

package reservation.quandoo.com.quandooreservation.presentation.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import reservation.quandoo.com.quandooreservation.R;
import reservation.quandoo.com.quandooreservation.data.local.Customer;

/**
 * Created by sohailaziz on 16/12/17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    public interface OnCustomerClickListener {
        void onCustomerClicked(Customer customer);
    }


    public static final int TYPE_ITEM = 0;
    public static final int TYPE_HEADER = 1;

    private Context context;
    private List<Customer> customers = new ArrayList<>();
    private List<Customer> filteredCustomers = new ArrayList<>();
    private OnCustomerClickListener customerClickListener;
    private CustomerFilter customerFilter;

    public CustomerAdapter(Context context, OnCustomerClickListener customerClickListener) {
        this.context = context;
        this.customerClickListener = customerClickListener;

    }

    public void updateData(List<Customer> customers) {
        this.customers.clear();
        this.customers.addAll(customers);

        this.filteredCustomers.clear();
        this.filteredCustomers.addAll(customers);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_HEADER) {
            View header = LayoutInflater.from(context).inflate(R.layout.search_layout, parent, false);
            return new SearchViewHolder(header);

        } else if (viewType == TYPE_ITEM) {
            View root = LayoutInflater.from(context).inflate(R.layout.customer_layout_row, parent, false);
            return new CustomerViewHolder(root, customerClickListener);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SearchViewHolder) {
            SearchView searchView = ((SearchViewHolder) holder).searchView;
            searchView.requestFocus();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Toast.makeText(context, newText, Toast.LENGTH_SHORT).show();
                    getFilter().filter(newText);
                    return false;
                }
            });

        } else if (holder instanceof CustomerViewHolder) {

            ((CustomerViewHolder) holder).bind(customers.get(position - 1), customerClickListener);
        }

    }

    @Override
    public Filter getFilter() {
        if (customerFilter == null) {
            customerFilter = new CustomerFilter();

        }
        return customerFilter;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public int getItemCount() {
        return customers.size() + 1;
    }


    /**
     * SearchView holder for header view
     */
    static class SearchViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.search_view)
        SearchView searchView;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Customer viewholder for item view
     */
    static class CustomerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_view_customer_name)
        TextView textViewName;

        public CustomerViewHolder(View itemView, OnCustomerClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Customer customer, final OnCustomerClickListener customerClickListener) {

            textViewName.setText(customer.getCustomerFirstName() + " " + customer.getCustomerLastName());
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customerClickListener.onCustomerClicked(customer);

                }
            });
        }
    }

    /**
     * Filter class to perform customer search based on first/last name
     */
    private class CustomerFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            //if no searching
            if (charSequence == null || charSequence.length() == 0) {
                filterResults.count = filteredCustomers.size();
                filterResults.values = filteredCustomers;
                return filterResults;
            }

            List<Customer> filteredCustomerList = new ArrayList<>();
            for (Customer c : filteredCustomers) {
                if (c.getCustomerFirstName().toLowerCase().contains(charSequence) || c.getCustomerLastName().toLowerCase().contains(charSequence)) {
                    filteredCustomerList.add(c);
                }
            }

            filterResults.count = filteredCustomerList.size();
            filterResults.values = filteredCustomerList;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            customers = (ArrayList<Customer>) filterResults.values;
            notifyDataSetChanged();

        }
    }
}

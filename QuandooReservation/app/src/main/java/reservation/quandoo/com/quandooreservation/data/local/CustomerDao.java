package reservation.quandoo.com.quandooreservation.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 *  DAO provides all database operations for customer
 *
 * Created by sohailaziz on 19/12/17.
 */

@Dao
public interface CustomerDao {
    @Query("SELECT * FROM customer")
    Maybe<List<Customer>> getCustomers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCustomer(Customer customer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllCustomers(List<Customer> customers);
}

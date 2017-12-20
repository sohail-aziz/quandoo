package reservation.quandoo.com.quandooreservation.data;

import java.util.List;

import io.reactivex.Observable;
import reservation.quandoo.com.quandooreservation.data.local.Customer;

/**
 * Created by sohailaziz on 16/12/17.
 */

public interface Repository {

    Observable<List<Customer>> getCustomers();
    Observable<List<Boolean>> getTables();
}

package reservation.quandoo.com.quandooreservation.data;

import java.util.List;

import reservation.quandoo.com.quandooreservation.data.response.Customer;
import rx.Observable;

/**
 * Created by sohailaziz on 16/12/17.
 */

public interface Repository {

    Observable<List<Customer>> getCustomers();
    Observable<List<Boolean>> getTables();
}

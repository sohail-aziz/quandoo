package reservation.quandoo.com.quandooreservation.data;

import java.util.List;

import io.reactivex.Observable;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import reservation.quandoo.com.quandooreservation.data.local.Table;

/**
 * Main Repository interface
 *
 * Created by sohailaziz on 16/12/17.
 */

public interface Repository {

    Observable<List<Customer>> getCustomers();

    Observable<List<Table>> getTables();

    Observable<Table> updateTable(Table table);

    void resetAllTables();
}

package reservation.quandoo.com.quandooreservation.data;

import java.util.List;

import io.reactivex.Observable;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import retrofit2.http.GET;

/**
 * Quandoo REST API end points
 * <p>
 * Created by sohailaziz on 15/12/17.
 */

public interface QuandooAPI {

    @GET("customer-list.json")
    Observable<List<Customer>> getCustomers();

    @GET("table-map.json")
    Observable<List<Boolean>> getTables();

}


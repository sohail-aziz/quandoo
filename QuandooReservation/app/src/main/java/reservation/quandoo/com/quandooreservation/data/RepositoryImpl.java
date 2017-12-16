package reservation.quandoo.com.quandooreservation.data;

import java.util.List;

import javax.inject.Inject;

import reservation.quandoo.com.quandooreservation.data.QuandooAPI;
import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.data.response.Customer;
import rx.Observable;

/**
 * Created by sohailaziz on 16/12/17.
 */

public class RepositoryImpl implements Repository {


    private final QuandooAPI  quandooAPI;
    //local data source comes here

    @Inject
    public RepositoryImpl(QuandooAPI quandooAPI) {
        this.quandooAPI = quandooAPI;
    }


    @Override
    public Observable<List<Customer>> getCustomers() {
        return quandooAPI.getCustomers();
    }

    @Override
    public Observable<List<Boolean>> getTables() {
        return quandooAPI.getTables();
    }
}

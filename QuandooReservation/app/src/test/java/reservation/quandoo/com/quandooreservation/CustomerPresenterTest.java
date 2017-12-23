package reservation.quandoo.com.quandooreservation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.IllegalFormatCodePointException;

import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.presentation.presenter.CustomerPresenter;

/**
 * Tests CustomerPresenter
 * Created by sohailaziz on 23/12/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class CustomerPresenterTest {

    @Mock
    Repository mockRepository;
    @Mock
    ErrorMessageFactory mockErrorMessageFactory;

    @Test(expected = IllegalStateException.class)
    public void testExceptoinIfViewNotSet() {

        CustomerPresenter presenter = new CustomerPresenter(mockRepository, mockErrorMessageFactory);
        presenter.getCustomers();

    }
}

package reservation.quandoo.com.quandooreservation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.presentation.presenter.TablePresenter;

import static org.mockito.Mockito.mock;

/**
 * Tests TablePresenter
 * Created by sohailaziz on 23/12/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class TablePresenterTest {

    @Mock
    Repository mockRepository;
    @Mock
    ErrorMessageFactory mockErrorMessageFactory;
    TablePresenter tablePresenter;

    @Before
    public void setup() {
        tablePresenter = new TablePresenter(mockRepository, mockErrorMessageFactory);

    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsExceptionIfViewNotSet() {

        tablePresenter.getTablesData();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookTableThrowsExceptionOnNullArgument() {

        tablePresenter.bookTable(null, null);

    }
}

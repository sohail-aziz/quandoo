package reservation.quandoo.com.quandooreservation;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import reservation.quandoo.com.quandooreservation.presentation.view.MainActivity;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests Error Message factory
 * <p>
 * Created by sohailaziz on 23/12/17.
 */

@RunWith(AndroidJUnit4.class)
public class ErrorMessageFactoryTest {


    @Test
    public void testIOExceptionError() {

        Context context = InstrumentationRegistry.getTargetContext();

        String expectedError = context.getString(R.string.error_internet);

        ErrorMessageFactory errorMessageFactory = new ErrorMessageFactory(context);

        String actualError = errorMessageFactory.getErrorMessage(new IOException(""));

        assertThat(actualError, is(expectedError));

    }


}

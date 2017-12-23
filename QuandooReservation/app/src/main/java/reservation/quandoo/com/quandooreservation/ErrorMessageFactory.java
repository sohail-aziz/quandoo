package reservation.quandoo.com.quandooreservation;

import android.content.Context;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Error Message Factory to provide error message agains different exceptions
 *
 * Created by sohailaziz on 23/12/17.
 */


@Singleton
public  class ErrorMessageFactory {
    private final Context context;

    @Inject
    public ErrorMessageFactory(Context context) {
        this.context = context;
    }

    public String getErrorMessage(Throwable throwable) {
        String errorMessage = "Some error has occurred";
        if (throwable instanceof IOException) {

            errorMessage = context.getString(R.string.error_internet);
        }

        return errorMessage;
    }
}

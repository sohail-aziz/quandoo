package reservation.quandoo.com.quandooreservation.presentation.view;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * BaseActivity contains common functions
 * Created by sohailaziz on 23/12/17.
 */

public class BaseActivity extends AppCompatActivity {

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

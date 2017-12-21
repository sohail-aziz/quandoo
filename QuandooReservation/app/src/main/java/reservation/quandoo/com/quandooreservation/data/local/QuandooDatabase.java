package reservation.quandoo.com.quandooreservation.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by sohailaziz on 18/12/17.
 */

@Database(entities = {Customer.class,Table.class},version = 1)
public abstract class QuandooDatabase extends RoomDatabase{
    public abstract CustomerDao customerDao();
    public abstract TableDao tableDao();
}

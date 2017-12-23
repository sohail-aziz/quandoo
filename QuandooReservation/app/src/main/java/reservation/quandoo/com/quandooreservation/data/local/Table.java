package reservation.quandoo.com.quandooreservation.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Table entity for persistence
 *
 * Created by sohailaziz on 20/12/17.
 */

@Entity(tableName = "restaurant_table")
public class Table {

    @PrimaryKey
    private int id;
    private boolean isAvailable;

    public Table(int id, boolean isAvailable) {
        this.id = id;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

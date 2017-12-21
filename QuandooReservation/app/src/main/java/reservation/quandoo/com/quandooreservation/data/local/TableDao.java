package reservation.quandoo.com.quandooreservation.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by sohailaziz on 20/12/17.
 */

@Dao
public interface TableDao {

    @Query("SELECT * FROM resturant_table")
    Maybe<List<Table>> getTables() ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTable(Table table);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTables(List<Table> tables);

    @Update
    void updateTable(Table table);

    @Update
    void updateAllTables(List<Table> tables);


}

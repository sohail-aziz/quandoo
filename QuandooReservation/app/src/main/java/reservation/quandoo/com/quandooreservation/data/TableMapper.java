package reservation.quandoo.com.quandooreservation.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import reservation.quandoo.com.quandooreservation.data.local.Table;

/**
 * Maps Boolean to Table
 * <p>
 * Created by sohailaziz on 23/12/17.
 */

@Singleton
public final class TableMapper {

    @Inject
    public TableMapper() {
    }

    /**
     * Maps boolean to table
     *
     * @param isAvailable
     * @param id          id of the table
     * @return table {@link Table}
     */
    public Table map(boolean isAvailable, int id) {
        return new Table(id, isAvailable);
    }


    /**
     * Maps boolean list to table list
     *
     * @param tablesStates list of boolean
     * @return  List of {@link Table}
     *
     */
    public List<Table> map(List<Boolean> tablesStates) {
        List<Table> tables = Collections.emptyList();

        if (tablesStates != null) {
            int id = 0;
            tables = new ArrayList<>(tablesStates.size());
            for (Boolean b : tablesStates) {
                tables.add(new Table(id, b));
                ++id;
            }
        }

        return tables;
    }

}

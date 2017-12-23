package reservation.quandoo.com.quandooreservation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import reservation.quandoo.com.quandooreservation.data.TableMapper;
import reservation.quandoo.com.quandooreservation.data.local.Table;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests TableMapper
 *
 * Created by sohailaziz on 23/12/17.
 */

@RunWith(JUnit4.class)
public class TableMapperTest {

    TableMapper tableMapper;

    @Before
    public void setup() {
        tableMapper = new TableMapper();
    }

    @Test
    public void testSingleObjectMapping() {

        Boolean isAvailable = false;
        int id = 0;
        Table table = tableMapper.map(isAvailable, id);

        assertThat("matches id", table.getId() == id);
        assertThat("matches state", table.isAvailable() == isAvailable);

    }

    @Test
    public void testArrayMapping() {

        List<Boolean> tableStates = new ArrayList<>();
        tableStates.add(0, true);
        tableStates.add(1, false);
        tableStates.add(2, false);

        List<Table> tableList = tableMapper.map(tableStates);

        assertThat("size is correct", tableList.size() == 3);
        assertThat("first element is true", tableList.get(0).isAvailable() == true);
        assertThat("last element is false", tableList.get(2).isAvailable() == false);
    }
}

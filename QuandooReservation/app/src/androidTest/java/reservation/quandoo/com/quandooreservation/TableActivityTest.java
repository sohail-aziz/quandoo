package reservation.quandoo.com.quandooreservation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import reservation.quandoo.com.quandooreservation.data.Repository;
import reservation.quandoo.com.quandooreservation.data.local.Customer;
import reservation.quandoo.com.quandooreservation.data.local.Table;
import reservation.quandoo.com.quandooreservation.presentation.view.MainActivity;
import reservation.quandoo.com.quandooreservation.presentation.view.TableActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by sohailaziz on 22/12/17.
 */

@RunWith(AndroidJUnit4.class)
public class TableActivityTest {

    String FIRST_NAME = "sohail";
    String LAST_NAME = "aziz";

    @Rule
    public ActivityTestRule<TableActivity> mActivityRule =
            new ActivityTestRule<TableActivity>(TableActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Customer customer = new Customer(FIRST_NAME, LAST_NAME, 0);
                    Intent result = TableActivity.getCallingIntent(targetContext, customer);
                    return result;
                }
            };


    @Test
    public void testCustomerNameIsSameAsReceivedInIntent() {

        String expectedCustomerTitle = FIRST_NAME + " " + LAST_NAME;
        onView(withId(R.id.text_view_customer_name)).check(matches(withText(expectedCustomerTitle)));
    }

    @Test
    public void testTable10IsAvailable() {



        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view_tables), isDisplayed()));

        //check table index 10 is available
        recyclerView.check(matches(atPosition(10, isEnabled())));

    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        //checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }



}

package reservation.quandoo.com.quandooreservation;

import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import reservation.quandoo.com.quandooreservation.presentation.view.MainActivity;
import reservation.quandoo.com.quandooreservation.presentation.view.TableActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v4.util.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


/**
 * Created by sohailaziz on 22/12/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public static final String TITLE = "Customer List";
    public static final String BILL_GATES = "Bill Gates";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testActivityTitleIsCorrect() {

        String title = mainActivityTestRule.getActivity().getTitle().toString();
        assertThat(title, is(TITLE));
    }

    @Test
    public void testRecyclerViewIsVisible() {
        onView(withId(R.id.recycler_view_customers)).check(matches(isDisplayed()));
    }

    @Test
    public void testHasFirstItem() {
        onView(withId(R.id.recycler_view_customers))
                .check(matches(atPosition(1, hasDescendant(withText("Marilyn Monroe")))));
    }


    @Test
    public void testFilteringBillGates() {

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view_customers), isDisplayed()));
        //recyclerView.perform(actionOnItemAtPosition(0, click()));

        recyclerView.perform(actionOnItemAtPosition(0, typeText("bill")));


        recyclerView.check(matches(hasDescendant(withText(BILL_GATES))));

    }

    @Test
    public void testItemClickLaunchesTableActivity() {
        Intents.init();

        onView(withId(R.id.recycler_view_customers)).perform(actionOnItemAtPosition(1, click()));

        intended(hasComponent(TableActivity.class.getName()), times(1));

        Intents.release();
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

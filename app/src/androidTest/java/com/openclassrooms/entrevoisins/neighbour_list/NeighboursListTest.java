
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {
    // This is fixed
    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule = new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /*@Rule
    public CountingIdlingResource mResource = new CountingIdlingResource("TEST");
    */


    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.list_neighbours)).check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT-1));
    }

    /**
     * When we click an item, we can see the screen of the neighbour detail
     */
    @Test
    public void myNeighboursList_shouldShowDetailScreen() {
        // When perform a click on a item
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0, click()));
        // Then : check if the avatar of the neighbour is displayed
        onView(withId(R.id.detail_avatar)).check(matches(isDisplayed()));
    }

    /**
     * When we are on the Detail Screen, the TextView of the user is not empty
     */
    @Test
    public void myNeighboursList_neighbourNameShouldNotBeEmpty() {
        // When perform a click on a item
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0,click()));
        // Then : check if the name is dispplayed
        onView(withId(R.id.detail_name)).check(matches(isDisplayed()));
        // And check if the name equal to the first neighbour in the neighbour list
        onView(withId(R.id.detail_name)).check(matches(withText("Caroline")));
    }

    /**
     * When Favorite Tab is selected, it only show favorites neighbours
     */
    @Test
    public void myNeighboursList_onlyShowFavoritesNeighbours() {
        try {
            // TRY IF THE LIST OF FAVORITES IS NOT EMPTY

            // Select the good tab
            Matcher<View> matcher = allOf(withText("FAVORITES"), isDescendantOfA(withId(R.id.tabs)));
            onView(matcher).perform(click());

            // When perform a click on a item
            onView(withId(R.id.list_favorites_neighbours)).perform(actionOnItemAtPosition(0,click()));
            // Then : check if the button of varite is displayed
            onView(withId(R.id.detail_fav_btn)).check(matches(isDisplayed()));
            // And check if the neighbour is favorite
            onView(allOf(withId(R.id.detail_fav_btn), withTagValue(is((Object) "isFavorite")))).check(matches(isDisplayed()));
        } catch (PerformException e) {
            // IF THE LIST OF FAVORITES IS EMPTY, WE HAD THE FIRST NEIGHBOUR IN THE LIST OF FAVORITE

            // Select the tab "MY NEIGHBOURS"
            Matcher<View> matcher = allOf(withText("MY NEIGHBOURS"), isDescendantOfA(withId(R.id.tabs)));
            onView(matcher).perform(click());

            // When perform a click on a item
            onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0,click()));
            // Then : perform a click on the fav button
            onView(withId(R.id.detail_fav_btn)).perform(click());
            // Close DetailPage
            pressBack();


            // Select the good tab
            Matcher<View> newMatcher = allOf(withText("FAVORITES"), isDescendantOfA(withId(R.id.tabs)));
            onView(newMatcher).perform(click());

            // When perform a click on a item
            onView(withId(R.id.list_favorites_neighbours)).perform(actionOnItemAtPosition(0,click()));
            // Then : check if the button of varite is displayed
            onView(withId(R.id.detail_fav_btn)).check(matches(isDisplayed()));
            // And check if the neighbour is favorite
            onView(allOf(withId(R.id.detail_fav_btn), withTagValue(is((Object) "isFavorite")))).check(matches(isDisplayed()));

        }
    }
}
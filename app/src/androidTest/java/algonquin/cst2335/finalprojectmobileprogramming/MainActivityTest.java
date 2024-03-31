package algonquin.cst2335.finalprojectmobileprogramming;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testSunriseSunsetLookup() {
        onView(withId(R.id.editTextLatitude)).perform(replaceText("45.4215"), closeSoftKeyboard());
        onView(withId(R.id.editTextLongitude)).perform(replaceText("-75.6972"), closeSoftKeyboard());

        onView(withId(R.id.btnLookup)).perform(click());

        onView(withId(R.id.textViewResult)).check(matches(not(withText(""))));
    }

    @Test
    public void testSaveLocationToFavorites() {
        onView(withId(R.id.editTextLatitude)).perform(click());
        onView(withId(R.id.editTextLatitude)).perform(replaceText("115"));
        onView(withId(R.id.editTextLongitude)).perform(replaceText("115"));
        onView(withId(R.id.editTextLocationName)).perform(replaceText("abcd"));
        onView(withId(R.id.btnLookup)).perform(click());
        onView(withId(R.id.buttonSaveLocation)).perform(click());
        onView(withId(R.id.btnShowFavorites)).perform(click());

        onView(withId(R.id.favoritesRecyclerView))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText("abcd"))))
                .check(matches(hasDescendant(withText("abcd"))));
    }





}

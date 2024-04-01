package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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

import algonquin.cst2335.finalprojectmobileprogramming.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchArtistTest {

    @Rule
    public ActivityScenarioRule<DeezerMainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(DeezerMainActivity.class);

    @Test
    public void searchArtistTest() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(5164);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction textInputEditText = onView(
allOf(withId(R.id.editTextSearch),
childAtPosition(
childAtPosition(
withId(R.id.textInputLayoutSearch),
0),
0),
isDisplayed()));
        textInputEditText.perform(click());

        ViewInteraction textInputEditText2 = onView(
allOf(withId(R.id.editTextSearch),
childAtPosition(
childAtPosition(
withId(R.id.textInputLayoutSearch),
0),
0),
isDisplayed()));
        textInputEditText2.perform(replaceText("epa"), closeSoftKeyboard());
        }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }

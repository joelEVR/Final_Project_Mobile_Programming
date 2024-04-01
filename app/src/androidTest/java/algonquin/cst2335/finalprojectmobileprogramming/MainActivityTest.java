package algonquin.cst2335.finalprojectmobileprogramming;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivityTing> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivityTing.class);
    @Test
    public void testSunriseSunsetLookup() {
        onView(withId(R.id.editTextLatitude)).perform(replaceText("45.4215"), closeSoftKeyboard());
        onView(withId(R.id.editTextLongitude)).perform(replaceText("-75.6972"), closeSoftKeyboard());

        onView(withId(R.id.btnLookup)).perform(click());

        onView(withId(R.id.textViewResult)).check(matches(not(withText(""))));
    }
    @Test
    public void testSavedLocation() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5518);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextLatitude), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextLatitude),isDisplayed()));
        appCompatEditText2.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextLongitude),isDisplayed()));
        appCompatEditText3.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextLocationName),isDisplayed()));
        appCompatEditText4.perform(replaceText("aa"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLookup), withText("Lookup"),isDisplayed()));
        materialButton.perform(click());

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.buttonSaveLocation), withText("Save Location"),isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnShowFavorites), withText("Show Favorites"),isDisplayed()));
        materialButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("aa"),
                        withParent(withParent(withId(R.id.favoritesRecyclerView))),
                        isDisplayed()));
        textView.check(matches(withText("aa")));
    }
    @Test
    public void testBackForUpdate() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5480);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextLatitude),isDisplayed()));
        appCompatEditText4.perform(replaceText("22"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextLongitude),isDisplayed()));
        appCompatEditText6.perform(replaceText("22"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.editTextLocationName),isDisplayed()));
        appCompatEditText8.perform(replaceText("bb"));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnLookup),isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.buttonSaveLocation),isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.btnShowFavorites),isDisplayed()));
        materialButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.favoritesRecyclerView)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction editText = onView(
                allOf(withId(R.id.editTextLocationName), withText("bb"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText.check(matches(withText("bb")));
    }
    public void deleteLocationTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5565);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextLatitude),isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextLatitude),isDisplayed()));
        appCompatEditText2.perform(replaceText("111"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextLongitude),isDisplayed()));
        appCompatEditText3.perform(replaceText("111"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextLocationName),isDisplayed()));
        appCompatEditText4.perform(replaceText("ccc"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLookup),isDisplayed()));
        materialButton.perform(click());

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.buttonSaveLocation),isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnShowFavorites),isDisplayed()));
        materialButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.delete_button),isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.favoritesRecyclerView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
    }
    public void searchKeywordOfLocationTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5634);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextLatitude),isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextLatitude),isDisplayed()));
        appCompatEditText2.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextLongitude),isDisplayed()));
        appCompatEditText3.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextLocationName),isDisplayed()));
        appCompatEditText4.perform(replaceText("aa"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLookup), withText("Lookup"),isDisplayed()));
        materialButton.perform(click());

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.buttonSaveLocation), isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextLatitude), isDisplayed()));
        appCompatEditText5.perform(replaceText("22"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextLatitude), isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.editTextLongitude), isDisplayed()));
        appCompatEditText7.perform(replaceText("22"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.editTextLongitude), isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.editTextLocationName), isDisplayed()));
        appCompatEditText9.perform(replaceText("bb"));

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.editTextLocationName),  isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnLookup), isDisplayed()));
        materialButton3.perform(click());

        pressBack();

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.buttonSaveLocation),  isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.btnShowFavorites), isDisplayed()));
        materialButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.searchEditText), isDisplayed()));
        appCompatEditText11.perform(replaceText("a"), closeSoftKeyboard());

        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("aa"),
                        withParent(withParent(withId(R.id.favoritesRecyclerView))),
                        isDisplayed()));
        textView.check(matches(withText("aa")));
    }
    public void deleteAllLocationsTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5711);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextLatitude),isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextLatitude),isDisplayed()));
        appCompatEditText2.perform(replaceText("111"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextLongitude),isDisplayed()));
        appCompatEditText3.perform(replaceText("111"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextLocationName),isDisplayed()));
        appCompatEditText4.perform(replaceText("aaa"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLookup), isDisplayed()));
        materialButton.perform(click());

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.buttonSaveLocation), isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextLatitude), isDisplayed()));
        appCompatEditText5.perform(replaceText("222"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextLatitude), isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.editTextLongitude),isDisplayed()));
        appCompatEditText7.perform(replaceText("222"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.editTextLongitude), isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.editTextLocationName),isDisplayed()));
        appCompatEditText9.perform(replaceText("bbb"));

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.editTextLocationName),  isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnLookup),  isDisplayed()));
        materialButton3.perform(click());

        pressBack();

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.buttonSaveLocation), isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.btnShowFavorites),isDisplayed()));
        materialButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.all_delete),isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(android.R.id.button1), withText("confirm")));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.favoritesRecyclerView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
    }

}

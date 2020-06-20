package om.example.stayhydrated;


import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IntegrationTests {
    // Define the activity to be tested


    @Rule
    public ActivityTestRule<HomeActivity> activity = new ActivityTestRule<>(HomeActivity.class);


//Helper Function

    public void AssertDataInHistory(int position, int historyDataType, String Text) {
        onData(anything()) // Select all elements in a list
                .inAdapterView(withId(R.id.historyListView)) // Only in the historyListViewAdapter
                .atPosition(position)  // Select the element at specific position
                .onChildView(withId(historyDataType))  // Select the type of view  in the element
                .check(matches(withText(Text)));  // Assertion
    }


    public void progressBarValue(int value) {
        ProgressBar progressBar = activity.getActivity().findViewById(R.id.progressBar);
        assertThat(progressBar.getProgress(), equalTo(value));
    }

    public void ButtonClick(int id) {
        onView(withId(id)).perform(click());

    }

    public void doubleClickAtPosition(int position) {
        onData(anything())
                .inAdapterView(withId(R.id.historyListView))
                .atPosition(position)
                .perform(doubleClick());
    }


    public void assertListLength(int length) {
        ListView listView = activity.getActivity().findViewById(R.id.historyListView);
        assertThat(listView.getCount(), equalTo(length));
    }

    public void timestampTest() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm - dd/MM");
        String date = df.format(new Date());

        onData(anything())
                .inAdapterView(withId(R.id.historyListView))
                .atPosition(0)
                .onChildView(withId(R.id.historyDataTimestamp))
                .check(matches(withText(date)));
    }


    public void testImage(int position, int id) {
        onData(anything())
                .inAdapterView(withId(R.id.historyListView))
                .atPosition(position)
                .onChildView(withId(R.id.historyDataImage))
                .check(matches(withTagValue(Matchers.<Object>equalTo(id))));
    }

    //Real Tests
    //Feature 1
    //Scenario 1
    @Test
    public void OneElementAdded() {
        ListView historyListView = activity.getActivity().findViewById(R.id.historyListView);
        //Drop image clicked
        ButtonClick(R.id.dropletImage);

        //Checking element at last position and verifying image
        onData(anything()) // Select all elements in a list
                .inAdapterView(withId(R.id.historyListView)) // Only in the historyListViewAdapter
                .atPosition(historyListView.getCount() - 1)  // Select the element at index 0
                .onChildView(withId(R.id.historyDataImage))  // Select the view historyDataImage in the element
                .check(matches(withTagValue(Matchers.<Object>equalTo(R.drawable.icons8_water_droplet))));  // Assertion
    }

    //Scenario 2
    @Test
    public void TwoElementAdded() {

        ListView historyListView = activity.getActivity().findViewById(R.id.historyListView);

        //Drop image clicked
        ButtonClick(R.id.dropletImage);
        //bottle image clicked
        ButtonClick(R.id.bottleImage);

        //Checking element at last position have image
        testImage(historyListView.getCount() - 1, R.drawable.icons8_water_bottle);

    }

    //Feature 2
    //Scenario 1
    @Test
    public void checkingTextWithQuantity() {

        ListView historyListView = activity.getActivity().findViewById(R.id.historyListView);
        //glass image clicked
        ButtonClick(R.id.glassImage);


        //verifying text at the top position
        AssertDataInHistory(historyListView.getCount() - 1, R.id.historyDataText, "200mL");

        SimpleDateFormat df = new SimpleDateFormat("HH:mm - dd/MM");
        String date = df.format(new Date());

        //verifying timeStamp
        AssertDataInHistory(historyListView.getCount() - 1, R.id.historyDataTimestamp, date);

    }

    @Test
    //Scenario 2
    public void checkingTextWithAlreadyPresentQuantities() {
        ListView historyListView = activity.getActivity().findViewById(R.id.historyListView);
        //Drop image clicked
        ButtonClick(R.id.dropletImage);
        //glass image clicked
        ButtonClick(R.id.glassImage);

        //Drop image clicked
        ButtonClick(R.id.dropletImage);

        //verifying text at the top position
        AssertDataInHistory(historyListView.getCount() - 1, R.id.historyDataText, "50mL");

        SimpleDateFormat df = new SimpleDateFormat("HH:mm - dd/MM");
        String date = df.format(new Date());

        //verifying timeStamp
        AssertDataInHistory(historyListView.getCount() - 1, R.id.historyDataTimestamp, date);


    }

    //Feature 3
    //Scenario 1
    @Test
    public void checkProgressBar() {

        int progress = activity.getActivity().currentProgress();

        //deleting table and again creating by getting database from HomeActivity
        DatabaseHelper myDB=activity.getActivity().myDB;
        myDB.deleteTable();

        //Drop image clicked
        ButtonClick(R.id.dropletImage);
        //glass image clicked
        ButtonClick(R.id.glassImage);

        progressBarValue(progress+250);
    }

    //Scenario 2
    @Test
    public void checkProgressBar2() {
        //Bottle image clicked
        ButtonClick(R.id.bottleImage);
        ButtonClick(R.id.bottleImage);
        ButtonClick(R.id.bottleImage);
        ButtonClick(R.id.bottleImage);

        ButtonClick(R.id.bottleImage);
        ButtonClick(R.id.bottleImage);
        ButtonClick(R.id.bottleImage);

        ButtonClick(R.id.dropletImage);


        progressBarValue(2500);
    }


    @BeforeClass
    public static void beforeClass() {
        InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseHelper.Database_Name);
    }

    @After
    public void afterClass(){
        DatabaseHelper myDB=activity.getActivity().myDB;
        myDB.deleteTable();
    }
    //Feature 4
    //Scenario 1
    @Test
    public void deleteElement() throws InterruptedException {

        ListView historyListView = activity.getActivity().findViewById(R.id.historyListView);



        int progress = activity.getActivity().currentProgress();
        //Bottle image clicked
        ButtonClick(R.id.dropletImage);



        //Double click element with drop image
        doubleClickAtPosition(historyListView.getCount() - 1);

        Thread.sleep(2000);

        //Checking item removed
        assertListLength(historyListView.getCount());


        //checking progress bar
        progressBarValue(0);
    }

    //Scenario 2
    @Test
    public void deleteGlassImage() {

        ListView historyListView = activity.getActivity().findViewById(R.id.historyListView);

        //deleting table and again creating by getting database from HomeActivity
        int ini = activity.getActivity().currentProgress();
        DatabaseHelper myDB=activity.getActivity().myDB;
        myDB.deleteTable();

        //getting progress bar value
        int progress = activity.getActivity().currentProgress();

        //drop and glass
        ButtonClick(R.id.dropletImage);

        ButtonClick(R.id.glassImage);

        //Double click element with glass image
        doubleClickAtPosition(historyListView.getCount()-1);

        //Checking item removed
        assertListLength(historyListView.getCount());

        //checking progress bar
        progressBarValue(progress+50);

    }

    //extra intgegrations tests
    @Test
    public void doubleTap() {
        ButtonClick(R.id.dropletImage);
        doubleClickAtPosition(0);
    }


}

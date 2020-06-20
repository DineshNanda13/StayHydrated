package om.example.stayhydrated;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    ListView historyListView;
    ArrayList<ThreeQuantities> threeQuantitiesList = new ArrayList<ThreeQuantities>();
    HistoryListView historyListViewAdapter;
    ProgressBar progressBar;
    int totalQuantity = 0;
    long mLastClickTime = 0;

    //Database variables
    DatabaseHelper myDB;
    //Getting TimeStamp
    SimpleDateFormat df = new SimpleDateFormat("HH:mm - dd/MM");
    String date = df.format(new Date());
    private String timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //comparing dates everyday


        historyListViewAdapter = new HistoryListView(this, threeQuantitiesList);

        // Set up history list adapter
        historyListView = findViewById(R.id.historyListView);
        historyListView.setAdapter(historyListViewAdapter);

        // Set up the progress bar and give it a default progress
        progressBar = findViewById(R.id.progressBar);

        // The three quantity buttons
        ImageView dropletImage = findViewById(R.id.dropletImage);
        ImageView glassImage = findViewById(R.id.glassImage);
        ImageView bottleImage = findViewById(R.id.bottleImage);

        //Getting Database
        myDB = new DatabaseHelper(this);
        myDB.getReadableDatabase();

        //updating listview according to database whenever the application restarts
        updateList();

        // On click listeners
        dropletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timestamp = date;
                myDB.addQuantity("50", timestamp, regularCurrentDate());
                lastElement();
            }
        });

        glassImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timestamp = date;
                myDB.addQuantity("200", timestamp, regularCurrentDate());
                lastElement();
            }
        });

        bottleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timestamp = date;
                myDB.addQuantity("350", timestamp, regularCurrentDate());
                lastElement();
            }
        });

        //doubleClicking listView elements to remove elements
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                long currTime = System.currentTimeMillis();
                if (mLastClickTime != 0) {
                    if (currTime - mLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                        onItemDoubleClick(adapterView, view, i, l);
                    }
                }

                mLastClickTime = currTime;
            }

            private void onItemDoubleClick(AdapterView<?> adapterView, View view, int position, long l) {

                ThreeQuantities threeQuantities = (ThreeQuantities) adapterView.getItemAtPosition(position);

                myDB.deleteQuantity(threeQuantities.getId());

                //Removing data from listView
                threeQuantitiesList.remove(position);
                //updating progress bar
                int quantityReduced = Integer.parseInt(threeQuantities.get_quantity());
                totalQuantity -= quantityReduced;
                progressBar.setProgress(totalQuantity);
                //setting up changes
                historyListViewAdapter.notifyDataSetChanged();
            }
        });


    }

    //Helper Functions to update the data from the database

    //Getting all data from list
    private void updateList() {
        threeQuantitiesList.clear();
        List<ThreeQuantities> threeQuantities = myDB.getAllQuantities();
        totalQuantity = 0;

        //checking date and then deleting database, if next day comes
        deleteDatabaseAfter24Hours(threeQuantities);

        //looping through threeQuantitiesList variables and retreiving data
        for (ThreeQuantities tq : threeQuantities) {
            threeQuantitiesList.add(tq);
            int quantity = Integer.parseInt(tq.get_quantity());
            totalQuantity += quantity;
            progressBar.setProgress(totalQuantity);
            historyListViewAdapter.notifyDataSetChanged();
        }


    }


    //Getting data of last item only
    public void lastElement() {
        ThreeQuantities threeQuantity = myDB.lastItem();

        //setting up progress bar
        int quantity = Integer.parseInt(threeQuantity.get_quantity());
        totalQuantity += quantity;
        progressBar.setProgress(totalQuantity);

        threeQuantitiesList.add(threeQuantity);
        historyListViewAdapter.notifyDataSetChanged();
    }

    //current progress bar value calculated from database
    public int currentProgress() {
        List<ThreeQuantities> threeQuantities = myDB.getAllQuantities();
        totalQuantity = 0;

        for (ThreeQuantities tq : threeQuantities) {
            int quantity = Integer.parseInt(tq.get_quantity());
            totalQuantity += quantity;
        }
        return totalQuantity;
    }

    // today date function, which return today date in string format
    private String regularCurrentDate() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date());
        return date;
    }

    private void deleteDatabaseAfter24Hours(List<ThreeQuantities> threeQuantities) {
        if (threeQuantities.size() > 0) {
            ThreeQuantities TQ = threeQuantities.get(0);
            String dateFromDatabase = TQ.getRegularDate();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Date strDateFromDatabase = null;
            Date td = null;
            String todayDate = sdf.format(new Date());
            try {
                td = sdf.parse(todayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                strDateFromDatabase = sdf.parse(dateFromDatabase);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int returnedValue = td.compareTo(strDateFromDatabase);

            //Will return positive, if current date is greater than oldDate
            if (returnedValue > 0) {
                myDB.deleteTable();

            }
        }
    }
}

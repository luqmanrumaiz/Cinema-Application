package com.cinemaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RatingsActivity extends AppCompatActivity
{
    private ArrayList<String> movieTitles;
    private DatabaseHelper databaseHelper;
    private ListView listView;
    private final String API_KEY = "k_i41mf3oz" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        listView = (ListView) findViewById(R.id.ratingsMoviesListView);
        databaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView()
    {
        // Getting the Data from the Database and Adding it to a List to a list
        Cursor data = databaseHelper.getData();
        movieTitles = new ArrayList<>();

        // With the Cursor we can easily move through each row through the Data from the Table
        while(data.moveToNext())
        {
            // Titles exist in the Second Column at index 1
            movieTitles.add(data.getString(1));
        }

        /* Creating the List Adapter to interpret the ArrayList to the ListView and with each Row with
         * the simple_list_item_multiple_choice Layout to make each Row have a Checkbox for marking
         * favorites
         */
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, movieTitles);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);  // Gives the Ability to mark Multiple Checkboxes
        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            CheckedTextView checkedTextView = (CheckedTextView) view;
        });
    }

    /**
     *
     *
     * @param view The Button View
     */
    public void findRatings(View view)
    {

        Thread thread = new Thread(() ->
        {
            String content =
                    getJSONResult("https://imdb-api.com/en/API/SearchTitle/"+ API_KEY +
                            "/");

            JSONObject jsonObject;
            try
            {
                jsonObject = new JSONObject(content);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        });
        thread.start();

    }

    public String getJSONResult(final String query)
    {
        StringBuilder result = new StringBuilder();
        try
        {
            URL url = new URL(query);

            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null)

                result.append(line);

            rd.close();
            Log.i("JSON Result",result.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result.toString();
    }
}
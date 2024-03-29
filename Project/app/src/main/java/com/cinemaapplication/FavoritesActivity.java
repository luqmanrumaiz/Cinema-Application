package com.cinemaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity
{
    private ArrayList<String> movieTitles;
    private ArrayList<Boolean> favorites;
    private DatabaseHelper databaseHelper;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        listView = findViewById(R.id.favoriteMoviesListView);
        databaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView()
    {
        // Getting the Data from the Database and Adding it to a List to a list
        Cursor data = databaseHelper.getData();
        movieTitles = new ArrayList<>();
        favorites = new ArrayList<>();

        // With the Cursor we can easily move through each row through the Data from the Table
        while(data.moveToNext())
        {
            /* The 8th Column or the 7th Index of this Cursor Data contains an Integer representing
             * the Favorite Boolean so if it is 1 it is favorite and should be added to the Movie
             * Titles */
            if (data.getInt(7) == 1)
            {
                // Titles exist in the Second Column at index 1
                movieTitles.add(data.getString(1));

                // Adding the boolean determining whether the movie is favorite or not (1 if true else false)
                favorites.add(true);
            }
        }

        /* Creating the List Adapter to interpret the ArrayList to the ListView and with each Row with
         * the simple_list_item_multiple_choice Layout to make each Row have a Checkbox for marking
         * favorites
         */
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, movieTitles);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);  // Gives the Ability to mark Multiple Checkboxes
        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            CheckedTextView checkedTextView = (CheckedTextView) view;
            favorites.set(position, checkedTextView.isChecked());
        });

        // For Loop that Updates the Checkboxes based on if the Movie is favorite or not
        int count = 0;
        for (Boolean favorite : favorites)
        {
            listView.setItemChecked(count, favorite);
            count ++;
        }
    }

    /**
     * This Method saves the Booleans in the favorites ArrayList which correspond to if a Movie
     * is a favorite or not in to the SQLite Database
     *
     * @param view The Button View
     */
    public void saveFavorites(View view)
    {
        int count = 0;
        for (Boolean favorite : favorites)
        {
            // SQLite does not support Booleans therefore it is represented as 1 for true and 0 for false
            if (favorite)

                databaseHelper.makeFavorite(movieTitles.get(count), 1);

            else databaseHelper.makeFavorite(movieTitles.get(count), 0);

            count ++;
        }

        // If no favorites are found an Error message is shown
        if (favorites.size() > 0)

            Snackbar.make(view, "Saved !",Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.yellow))
                    .setTextColor(getResources().getColor(R.color.black))
                    .show();

        else Snackbar.make(view, "There are no Movies !",Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.yellow))
                    .setTextColor(getResources().getColor(R.color.black))
                    .show();

        populateListView();
    }
}
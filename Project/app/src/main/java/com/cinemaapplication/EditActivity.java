package com.cinemaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity
{
    private ArrayList<String> movieTitles;
    private ArrayList<Boolean> favorites;
    private ArrayList<Movie> registeredMovies;
    private DatabaseHelper databaseHelper;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        listView = findViewById(R.id.editRegisteredMoviesListView);
        databaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView()
    {
        // Getting the Data from the Database and Adding it to a List to a list
        Cursor data = databaseHelper.getData();
        movieTitles = new ArrayList<>();
        favorites = new ArrayList<>();

        registeredMovies = new ArrayList<>();

        // With the Cursor we can easily move through each row through the Data from the Table
        while(data.moveToNext())
        {
            System.out.println(data.getString(0));
            registeredMovies.add(new Movie(data.getString(1), data.getString(2),
                    data.getString(3), data.getString(4), data.getInt(5),
                    data.getString(6), data.getInt(7) == 1 ));

            movieTitles.add(data.getString(1));
        }

        /* Creating the List Adapter to interpret the ArrayList to the ListView and with each Row with
         * the simple_list_item_multiple_choice Layout to make each Row have a Checkbox for marking
         * favorites
         */
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieTitles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent intent = new Intent(this, EditInfoActivity.class);

            intent.putExtra("title", registeredMovies.get(position).getTitle());
            intent.putExtra("year", registeredMovies.get(position).getYear());
            intent.putExtra("director", registeredMovies.get(position).getDirector());
            intent.putExtra("actorActress", registeredMovies.get(position).getActorActress());
            intent.putExtra("rating", registeredMovies.get(position).getRating());
            intent.putExtra("review", registeredMovies.get(position).getReview());
            intent.putExtra("favorite", registeredMovies.get(position).isFavorite());

            startActivity(intent);
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
     * is a favorite or not in to the SQLLite Database
     *
     * @param view The Button View
     */
    public void saveFavorites(View view)
    {
        int count = 0;
        for (Boolean favorite : favorites)
        {
            // SQLLite does not support Booleans therefore it is represented as 1 for true and 0 for false
            if (favorite)

                databaseHelper.makeFavorite(movieTitles.get(count), 1);

            else databaseHelper.makeFavorite(movieTitles.get(count), 0);

            count ++;
        }
    }
}
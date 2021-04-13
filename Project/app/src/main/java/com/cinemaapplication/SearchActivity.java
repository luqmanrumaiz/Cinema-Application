package com.cinemaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ListView listView;
    TextInputLayout searchInputLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = findViewById(R.id.searchMoviesListView);

        searchInputLayout = findViewById(R.id.searchTextField);
    }

    public void searchMovie(View view)
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String searchData = searchInputLayout.getEditText().getText().toString().trim();

        // Getting the Data from the Database and Adding it to a List to a list
        Cursor data = databaseHelper.getSearchedData(searchData);
        ArrayList<String> movieInformation = new ArrayList<>();

        // With the Cursor we can easily move through each row through the Data from the Table
        while(data.moveToNext())
        {
            movieInformation.add(
                            "Title:  " + data.getString(1) + "\n" +
                            "Year:  " + data.getString(2) + "\n" +
                            "Director:  " + data.getString(3) + "\n" +
                            "Actor/Actress:  " + data.getString(4) + "\n" +
                            "Rating:  " + data.getString(5) + "\n" +
                            "Review:  " + data.getString(6) + "\n" +
                            "Favorite:  " + (Integer.parseInt(data.getString(7)) == 1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieInformation);
        listView.setAdapter(adapter);
    }
}
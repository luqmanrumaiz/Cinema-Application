package com.cinemaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<String> movieTitles;
    private DatabaseHelper databaseHelper;
    private ListView listView;
    TextInputLayout searchInputLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = (ListView) findViewById(R.id.searchMoviesListView);
        databaseHelper = new DatabaseHelper(this);
        searchInputLayout = findViewById(R.id.searchTextField);
    }

    public void searchMovie(View view)
    {
        String searchData = searchInputLayout.getEditText().getText().toString().trim();

        // Getting the Data from the Database and Adding it to a List to a list
        Cursor data = databaseHelper.getSearchedData(searchData);
        movieTitles = new ArrayList<>();

        // With the Cursor we can easily move through each row through the Data from the Table
        while(data.moveToNext())
        {
            movieTitles.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieTitles);
        listView.setAdapter(adapter);
    }
}
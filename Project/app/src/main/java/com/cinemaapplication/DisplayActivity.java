package com.cinemaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class DisplayActivity extends AppCompatActivity
{
    String[] movieTitles = {"ABC", "BAC", "CBA"};
    Movie[] movies;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        listView = findViewById(R.id.registeredMoviesListView);
        setListView();
    }

    public void setListView()
    {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, movieTitles);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            listView.getItemAtPosition(position);
            CheckedTextView checkedTextView = ((CheckedTextView)view);
            System.out.println(checkedTextView.isChecked() + "" + position);
        });
    }
}


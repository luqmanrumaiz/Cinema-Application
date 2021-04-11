package com.cinemaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity
{
    String[] registeredMoviesTitles = {"ABC"};
    List<Movie> registeredMovies;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        listView = findViewById(R.id.editRegisteredMoviesListView);
        setListView();
    }

    public void setListView()
    {
        registeredMovies = new ArrayList<>();
        registeredMovies.add(new Movie("ABC", "2020-12-12", "GREAT", "LUQMAN", "sad", 10, false));

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, registeredMoviesTitles);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("title", registeredMovies.get(position).getTitle());
            intent.putExtra("year", registeredMovies.get(position).getYear());
            intent.putExtra("director", registeredMovies.get(position).getDirector());
            intent.putExtra("title", registeredMovies.get(position).getRating());
            intent.putExtra("title", registeredMovies.get(position).getReview());
            startActivity(intent);
        });
    }
}
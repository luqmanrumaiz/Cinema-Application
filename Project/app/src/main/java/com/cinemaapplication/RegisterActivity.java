package com.cinemaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity
{
    DatabaseHelper databaseHelper;

    TextInputLayout titleInputLayout;
    TextInputLayout dateInputLayout;
    TextInputLayout directorInputLayout;
    TextInputLayout actorActressInputLayout;
    TextInputLayout reviewInputLayout;
    TextInputLayout ratingInputLayout;
    String title;
    String year;
    String director;
    String actorActress;
    String review;
    int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // All Material TextFields
        titleInputLayout = findViewById(R.id.titleTextField);
        dateInputLayout = findViewById(R.id.yearTextField);
        directorInputLayout = findViewById(R.id.directorTextField);
        actorActressInputLayout = findViewById(R.id.actorAndActressTextField);
        reviewInputLayout = findViewById(R.id.reviewTextField);
        ratingInputLayout = findViewById(R.id.ratingTextField);
    }

    /**
     * This Method saves the details of a Movie to the SQLLite Database, it also Validates User Input
     *
     * @param view Button View
     */
    public void registerMovie(View view)
    {
        databaseHelper = new DatabaseHelper(this);

        // Getting all String and Integer Values from the TextFields
        title = titleInputLayout.getEditText().getText().toString().trim();
        year = dateInputLayout.getEditText().getText().toString().trim();
        director = directorInputLayout.getEditText().getText().toString().trim();
        actorActress = actorActressInputLayout.getEditText().getText().toString().trim();
        review = reviewInputLayout.getEditText().getText().toString().trim();
        rating = Integer.parseInt(ratingInputLayout.getEditText().getText().toString().trim());

        System.out.println(rating);

        databaseHelper.addData(new Movie(title, year, director, actorActress, rating, review, false));

        titleInputLayout.setPlaceholderText("");
        dateInputLayout.setPlaceholderText("");
        directorInputLayout.setPlaceholderText("");
        actorActressInputLayout.setPlaceholderText("");
        reviewInputLayout.setPlaceholderText("");
        ratingInputLayout.setPlaceholderText("");
    }
}

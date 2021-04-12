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
    private DatabaseHelper databaseHelper;

    private TextInputLayout titleInputLayout;
    private TextInputLayout dateInputLayout;
    private TextInputLayout directorInputLayout;
    private TextInputLayout actorActressInputLayout;
    private TextInputLayout reviewInputLayout;
    private TextInputLayout ratingInputLayout;
    private String title;
    private String year;
    private String director;
    private String actorActress;
    private String review;
    int rating;
    private boolean error;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // All Material TextFields
        titleInputLayout = findViewById(R.id.titleTextField);
        titleInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(titleInputLayout.getEditText(), titleInputLayout, this));

        dateInputLayout = findViewById(R.id.yearTextField);
        dateInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(dateInputLayout.getEditText(), dateInputLayout, this));


        directorInputLayout = findViewById(R.id.directorTextField);
        directorInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(directorInputLayout.getEditText(), directorInputLayout, this));

        actorActressInputLayout = findViewById(R.id.actorAndActressTextField);
        actorActressInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(actorActressInputLayout.getEditText(), actorActressInputLayout, this));

        reviewInputLayout = findViewById(R.id.reviewTextField);
        reviewInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(reviewInputLayout.getEditText(), reviewInputLayout, this));

        ratingInputLayout = findViewById(R.id.ratingTextField);
        ratingInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(ratingInputLayout.getEditText(), ratingInputLayout, this));

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

        databaseHelper.addData(new Movie(title, year, director, actorActress, rating, review, false));

        titleInputLayout.setPlaceholderText("");
        dateInputLayout.setPlaceholderText("");
        directorInputLayout.setPlaceholderText("");
        actorActressInputLayout.setPlaceholderText("");
        reviewInputLayout.setPlaceholderText("");
        ratingInputLayout.setPlaceholderText("");
    }

    public void setError(boolean error)
    {
        this.error = error;
    }
}

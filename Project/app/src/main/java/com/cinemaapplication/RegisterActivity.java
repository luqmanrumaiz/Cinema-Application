package com.cinemaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity
{
    private TextInputLayout titleInputLayout;
    private TextInputLayout dateInputLayout;
    private TextInputLayout directorInputLayout;
    private TextInputLayout actorActressInputLayout;
    private TextInputLayout reviewInputLayout;
    private TextInputLayout ratingInputLayout;
    private int rating;
    private boolean[] errors;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        errors = new boolean[]{false, false, false, false, false, false};

        // All Material TextFields
        titleInputLayout = findViewById(R.id.titleTextField);
        titleInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(titleInputLayout, RegisterActivity.this));

        dateInputLayout = findViewById(R.id.yearTextField);
        dateInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(dateInputLayout, RegisterActivity.this));


        directorInputLayout = findViewById(R.id.directorTextField);
        directorInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(directorInputLayout, RegisterActivity.this));

        actorActressInputLayout = findViewById(R.id.actorAndActressTextField);
        actorActressInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(actorActressInputLayout, RegisterActivity.this));

        reviewInputLayout = findViewById(R.id.reviewTextField);
        reviewInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(reviewInputLayout, RegisterActivity.this));

        ratingInputLayout = findViewById(R.id.ratingTextField);
        ratingInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(ratingInputLayout, RegisterActivity.this));

    }

    /**
     * This Method saves the details of a Movie to the SQLLite Database, it also Validates User Input
     *
     * @param view Button View
     */
    public void registerMovie(View view)
    {
        if (isError())

            Snackbar.make(view, "Please Resolve all Errors !!!",Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.transparent_yellow))
                    .setTextColor(getResources().getColor(R.color.grey_black))
                    .show();

        else
        {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);

            // Getting all String and Integer Values from the TextFields
            String title = titleInputLayout.getEditText().getText().toString().trim();
            String year = dateInputLayout.getEditText().getText().toString().trim();
            String director = directorInputLayout.getEditText().getText().toString().trim();
            String actorActress = actorActressInputLayout.getEditText().getText().toString().trim();
            String review = reviewInputLayout.getEditText().getText().toString().trim();
            String ratingString = ratingInputLayout.getEditText().getText().toString().trim();

            System.out.println(rating);

            if (title.equals("") || year.equals("") || director.equals("") || actorActress.equals("") ||
                review.equals("") || ratingString.equals("") )

                Snackbar.make(view, "Please Enter all Fields !!!",Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.transparent_yellow))
                        .setTextColor(getResources().getColor(R.color.grey_black))
                        .show();

            else
            {
                rating = Integer.parseInt(ratingString);
                databaseHelper.addData(new Movie(title, year, director, actorActress, rating,
                        review, false), view);


            }
        }
    }

    public boolean isError()
    {
        for (boolean error : errors)
        {
            if (error)

                return true;
        }
        return false;
    }

    public void setError(boolean error, int index)
    {
        this.errors[index] = error;
    }
}

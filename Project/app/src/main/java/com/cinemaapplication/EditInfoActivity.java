package com.cinemaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

public class EditInfoActivity extends AppCompatActivity
{
    private DatabaseHelper databaseHelper;
    private Movie movie;
    private int movieId;
    private TextInputLayout titleInputLayout;
    private TextInputLayout dateInputLayout;
    private TextInputLayout directorInputLayout;
    private TextInputLayout actorActressInputLayout;
    private TextInputLayout reviewInputLayout;
    private RatingBar ratingRatingBar;
    private SwitchMaterial favoriteSwitch;
    private boolean[] errors;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        errors = new boolean[]{false, false, false, false, false, false};

        Intent intent = getIntent();

        // Getting the Details of the Movie to Edit from the Intent
        movie = new Movie(intent.getStringExtra("title"), intent.getStringExtra("year"),
                intent.getStringExtra("director"), intent.getStringExtra("actorActress"),
                intent.getIntExtra("rating", 0), intent.getStringExtra("review"),
                intent.getBooleanExtra("favorite", false));

        movieId = intent.getIntExtra("movieId", 0);

        // All Material TextFields
        titleInputLayout = findViewById(R.id.titleTextField);
        titleInputLayout.getEditText().setText(movie.getTitle());
        titleInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(titleInputLayout, this));

        dateInputLayout = findViewById(R.id.yearTextField);
        dateInputLayout.getEditText().setText(movie.getYear());
        dateInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(dateInputLayout, this));

        directorInputLayout = findViewById(R.id.directorTextField);
        directorInputLayout.getEditText().setText(movie.getDirector());
        directorInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(directorInputLayout, this));

        actorActressInputLayout = findViewById(R.id.actorAndActressTextField);
        actorActressInputLayout.getEditText().setText(movie.getActorActress());
        actorActressInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(actorActressInputLayout, this));

        reviewInputLayout = findViewById(R.id.reviewTextField);
        reviewInputLayout.getEditText().setText(movie.getReview());
        reviewInputLayout.getEditText().addTextChangedListener(
                new CustomTextWatcher(reviewInputLayout, this));

        ratingRatingBar = findViewById(R.id.ratingBar1);
        ratingRatingBar.setRating(movie.getRating());

        favoriteSwitch = findViewById(R.id.favoriteSwitch);
        favoriteSwitch.setChecked(movie.isFavorite());

        databaseHelper = new DatabaseHelper(this);
    }

    /**
     * This Method saves the Booleans in the favorites ArrayList which correspond to if a Movie
     * is a favorite or not in to the SQLLite Database
     *
     * @param view The Button View
     */
    public void editMovie(View view)
    {
        if (isError())

            Snackbar.make(view, "Please Resolve all Errors !!!",Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.transparent_yellow))
                    .setTextColor(getResources().getColor(R.color.grey_black))
                    .show();

        else
        {
            // Getting all String and Integer Values from the TextFields
            String title = titleInputLayout.getEditText().getText().toString().trim();
            String year = dateInputLayout.getEditText().getText().toString().trim();
            String director = directorInputLayout.getEditText().getText().toString().trim();
            String actorActress = actorActressInputLayout.getEditText().getText().toString().trim();
            String review = reviewInputLayout.getEditText().getText().toString().trim();
            int rating = Math.round(ratingRatingBar.getRating());

            // If any of the Values from TextFields are empty an Error Message is shown
            if (title.equals("") || year.equals("") || director.equals("") || actorActress.equals("") ||
                    review.equals(""))

                Snackbar.make(view, "Fill all Empty TextFields !!!",Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.transparent_yellow))
                        .setTextColor(getResources().getColor(R.color.grey_black))
                        .show();

            else
            {
                System.out.println(rating);
                System.out.println(movieId);
                databaseHelper.editMovie(movieId + 1, new Movie(title, year, director, actorActress,
                        rating, review, favoriteSwitch.isChecked()));

            }

        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(EditInfoActivity.this, HomeActivity.class));
        finish();
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

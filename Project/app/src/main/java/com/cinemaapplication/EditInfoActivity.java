package com.cinemaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

public class EditInfoActivity extends AppCompatActivity
{
    DatabaseHelper databaseHelper;
    Movie movie;
    int movieId;
    TextInputLayout titleInputLayout;
    TextInputLayout dateInputLayout;
    TextInputLayout directorInputLayout;
    TextInputLayout actorActressInputLayout;
    TextInputLayout reviewInputLayout;
    RatingBar ratingRatingBar;
    SwitchMaterial favoriteSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        Intent intent = getIntent();

        movie = new Movie(intent.getStringExtra("title"), intent.getStringExtra("year"),
                intent.getStringExtra("director"), intent.getStringExtra("actorActress"),
                intent.getIntExtra("rating", 0), intent.getStringExtra("review"),
                intent.getBooleanExtra("favorite", false));

        movieId = intent.getIntExtra("movieId", 0);

        // All Material TextFields
        titleInputLayout = findViewById(R.id.titleTextField);
        titleInputLayout.getEditText().setText(movie.getTitle());

        dateInputLayout = findViewById(R.id.yearTextField);
        dateInputLayout.getEditText().setText(movie.getYear());

        directorInputLayout = findViewById(R.id.directorTextField);
        directorInputLayout.getEditText().setText(movie.getDirector());

        actorActressInputLayout = findViewById(R.id.actorAndActressTextField);
        actorActressInputLayout.getEditText().setText(movie.getActorActress());

        reviewInputLayout = findViewById(R.id.reviewTextField);
        reviewInputLayout.getEditText().setText(movie.getReview());

        ratingRatingBar = findViewById(R.id.ratingBar1);
        ratingRatingBar.setRating(movie.getRating());

        favoriteSwitch = findViewById(R.id.favoriteSwitch);

        System.out.println(movie.isFavorite());
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
        // Getting all String and Integer Values from the TextFields
        String title = titleInputLayout.getEditText().getText().toString().trim();
        String year = dateInputLayout.getEditText().getText().toString().trim();
        String director = directorInputLayout.getEditText().getText().toString().trim();
        String actorActress = actorActressInputLayout.getEditText().getText().toString().trim();
        String review = reviewInputLayout.getEditText().getText().toString().trim();
        int rating = (int) Math.round(ratingRatingBar.getRating());
        System.out.println(rating);

        databaseHelper.editMovie(movieId, new Movie(title, year, director, actorActress, rating, review, favoriteSwitch.isChecked()));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(EditInfoActivity.this, HomeActivity.class));
        finish();

    }
}

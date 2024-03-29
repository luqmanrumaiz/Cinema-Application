package com.cinemaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * This method navigates to 1 of the 4 Game Mode Pages based on the Button that has been clicked
     *
     * @param view
     */
    public void navigateFeatures(View view)
    {
        Intent intent = null;

        /* This if else condition checks whether the Id of the View Component is the same
         * as the Id of all 6 Buttons that indicate the Cinema Option to navigate to.
         */

        if (view.getId() == R.id.homeRegisterButton)

            intent = new Intent(this, RegisterActivity.class);

        else if (view.getId() == R.id.homeDisplayButton)

            intent = new Intent(this, DisplayActivity.class);

        else if (view.getId() == R.id.homeFavoriteButton)

            intent = new Intent(this, FavoritesActivity.class);

        else if (view.getId() == R.id.homeEditButton)

            intent = new Intent(this, EditActivity.class);

        else if (view.getId() == R.id.homeSearchButton)

            intent = new Intent(this, SearchActivity.class);

        else if (view.getId() == R.id.homeRatingsButton)

            intent = new Intent(this, RatingsActivity.class);

        startActivity(intent);
    }
}
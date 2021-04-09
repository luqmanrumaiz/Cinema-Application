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
        setContentView(R.layout.activity_main);
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
         * as the Id of all 4 Buttons that indicate the Game Mode to navigate to.
         */

        if (view.getId() == R.id.homeRegisterButton)

            intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);

    }
}
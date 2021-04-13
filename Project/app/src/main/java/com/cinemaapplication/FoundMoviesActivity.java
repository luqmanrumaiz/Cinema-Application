package com.cinemaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FoundMoviesActivity extends AppCompatActivity
{
    private ArrayList<String> foundMovieImageURls;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_movies);

        // Permitting all Network and Disk access of the Main Thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listView = findViewById(R.id.foundMoviesListView);
        populateListView();
    }

    /**
     * This Method is used to Populate the ListView with Items in this Case we will add all
     * the Movie Titles that are concatenated with the Rating
     */
    private void populateListView()
    {
        Intent intent = getIntent();

        ArrayList<String> foundMovies = intent.getStringArrayListExtra("foundMovies");
        foundMovieImageURls = intent.getStringArrayListExtra("foundMovieImageURls");

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foundMovies);
        listView.setAdapter(adapter);

        /* Setting an ItemClickListener that calls the showDialogWithMoviePoster to show the
         * Movie Poster in a Dialog, we also need to make the Dialog run on the Main
         * Thread so we use runOnUiThread to make sure the Dialog is not run on Worker Threads
         */
        listView.setOnItemClickListener((parent, view, position, id) ->

                runOnUiThread(() -> showDialogWithMoviePoster(position)));
    }

    /**
     * This Method shows an AlertDialog with an ImageView, and the the Image of the ImageView
     * is based on the ImageURL taken from the movieImageURLs List
     *
     * @param position The Position of the Current Movie in the ListView this also corresponds to
     *                 the index when getting the Movie Title or Image URL from the Lists
     */
    public void showDialogWithMoviePoster(int position)
    {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        /* Inflating a new View with the Custom Layout made for this AlertDialog and changing the
         * View of the AlertDialog to this Inflated View
         */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.image_dialog_layout, null);

        /* Try Catch Block that attempts to first parse the URL of the Movie Poster Image we want,
         * then attempts to connect to the URL using the openConnection Method and the
         * getInputStream Method attempts to get the Input Stream from that Socket. Finally the
         * static method decodeStream is accessed to decode the Input Stream in to a BitMap
         *
         * https://ranjithexpertisers.medium.com/load-image-from-url-in-android-studio-fe755a3348dd
         */
        URL url;
        Bitmap bmp = null;
        try
        {
            url = new URL(foundMovieImageURls.get(position));
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Setting the Bitmap to the ImageView in the AlertDialog
        ImageView imageView = dialogView.findViewById(R.id.moviePosterImage);
        imageView.setImageBitmap(bmp);

        dialog.setContentView(dialogView);
        dialog.show();
    }
}
package com.cinemaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RatingsActivity extends AppCompatActivity
{
    private ArrayList<String> movieTitles;
    private DatabaseHelper databaseHelper;
    private ListView listView;
    private ArrayList<String> foundMovies;
    private ArrayList<String> foundMovieImageURls;
    private AlertDialog alertDialog;
    private final String API_KEY = "k_fesodmof" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        listView = (ListView) findViewById(R.id.ratingsMoviesListView);
        databaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView()
    {
        // Getting the Data from the Database and Adding it to a List to a list
        Cursor data = databaseHelper.getData();
        movieTitles = new ArrayList<>();

        // With the Cursor we can easily move through each row through the Data from the Table
        while(data.moveToNext())
        {
            // Titles exist in the Second Column at index 1
            movieTitles.add(data.getString(1));
        }

        /* Creating the List Adapter to interpret the ArrayList to the ListView and with each Row with
         * the simple_list_item_multiple_choice Layout to make each Row have a Checkbox for marking
         * favorites
         */
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, movieTitles);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);  // Gives the Ability to mark Multiple Checkboxes
    }

    /**
     *
     *
     * @param view The Button View
     */
    public void findRatings(View view)
    {
        if (listView.getCheckedItemPosition() == -1)
        {
            Toast.makeText(this, "Please Select a Movie !!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            foundMovies = new ArrayList<>();
            foundMovieImageURls = new ArrayList<>();

            String checkedMovieTitle = movieTitles.get(listView.getCheckedItemPosition());
            loadMovieTitlesAndImageURLs(checkedMovieTitle);
        }
    }

    /**
     * This Method shows an AlertDialog with a ProgressBar that is used to show that something is
     * Loading (In cases where it takes time to get and process a lot of Data from the API it is used )
     */
    public void showDialogWithProgressBar()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        /* Inflating a new View with the Custom Layout made for this AlertDialog and changing the
         * View of the AlertDialog to this Inflated View
         */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_bar_dialog_layout, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     * This Method
     *
     * @param checkedMovieTitle
     */
    public void loadMovieTitlesAndImageURLs(String checkedMovieTitle)
    {
        // Creating a Separate Worker Thread to reduce load on Main UI Thread
        Thread thread = new Thread(() ->
        {
            /* Constructing our API call by concatenating the API_KEY with the movieId with the API,
             * then this is passed as the parameter for the called getJSONResult to get the JSON Body
             */
            String JSONResult =
                    getJSONResult("https://imdb-api.com/en/API/SearchTitle/"+ API_KEY +
                            "/" + checkedMovieTitle);

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(JSONResult);

                // The JSON Array that contains
                JSONArray movieResults = jsonObject.getJSONArray("results");

                RatingsActivity.this.runOnUiThread(this::showDialogWithProgressBar);

                /* Iterating through all the Movies from the movieResults JSON Array and storing
                 * needed details by taking the id, title and image attribute in the current item of the
                 * JSON Array as a JSON Object
                 */
                for( int movieCount = 0 ; movieCount < movieResults.length() ; movieCount ++ )
                {
                    JSONObject movie = movieResults.getJSONObject(movieCount);
                    foundMovies.add(movie.getString("title") + " (" +
                            getMovieRating(movie.getString("id")));

                    foundMovieImageURls.add(movie.getString("image"));
                    break;
                }

                // If not Movies Titles or Image URLs were found an Error Message is shown
                if (foundMovies == null || foundMovieImageURls == null)

                    Toast.makeText(this, "No Movies were Found !", Toast.LENGTH_SHORT).show();
                else
                {
                    alertDialog.dismiss();  // Closing the AlertDialog now that the Data is loaded

                    /* Making an Implicit Intent to the FoundMoviesActivity and adding the two
                     * ArrayLists as extra data to be received using putExtra with the Keys of the
                     * Extra Data as the ArrayLists' Variable name
                     */
                    Intent intent = new Intent(this, FoundMoviesActivity.class);
                    intent.putExtra("foundMovies", foundMovies);
                    intent.putExtra("foundMovieImageURls", foundMovieImageURls);
                    startActivity(intent);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    /**
     * This Method attempts to call an API to get the Rating of a specified Movie's Id
     *
     * @param movieId The ID of the Movie we want to Search for
     *
     * @return The Movie Rating
     */
    public String getMovieRating(String movieId)
    {
        /* Constructing our API call by concatenating the API_KEY with the movieId with the API,
         * then this is passed as the parameter for the called getJSONResult to get the JSON Body
         */
        String JSONResult =
                getJSONResult("https://imdb-api.com/en/API/UserRatings/"+ API_KEY +
                        "/" + movieId);

        JSONObject jsonObject;
        String rating;

        try
        {
            // Getting the totalRating property of the JSONObject
            jsonObject = new JSONObject(JSONResult);
            rating = jsonObject.getString("totalRating");

            /* Concatenating NF that means Not Found, if the rating is blank or null this API
             * returns the String null if a property is null
             */
            if (rating.equals("") || rating.equals("null"))

                rating += "NF";

            rating += ")";

            return rating;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This Method return a properly Constructed JSON Body based on an API that is to be called
     *
     * @param query The API that we want to query
     *
     * @return The JSON Body is Returned
     */
    public String getJSONResult(final String query)
    {
        StringBuilder result = new StringBuilder();
        try
        {
            URL url = new URL(query);

            // Attempting to connect to the URl with the openConnection Method
            URLConnection urlConnection = url.openConnection();

            /* Getting the Input Stream from the Socket with the getInputStream Method and then
             * setting it as a Parameter for the Constructor of a new InputStreamReader that is used
             * to decode bytes from the Input Stream to Characters that can be read by the BufferedReader.
             */
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            // Appending each Line from the bufferedReader to a StringBuilder construct the JSON Body
            String line;
            while ((line = bufferedReader.readLine()) != null)

                result.append(line);

            bufferedReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result.toString();
    }
}
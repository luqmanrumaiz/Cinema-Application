package com.cinemaapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "movie_manager";
    private static final String TABLE_NAME = "movie_table";
    private static final String COL1 = "movieId";
    private static final String COL2 = "title";
    private static final String COL3 = "year";
    private static final String COL4 = "director";
    private static final String COL5 = "actor_actress";
    private static final String COL6 = "rating";
    private static final String COL7 = "review";
    private static final String COL8 = "favorite";
    private final Context context;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    /**
     * This Overridden Method is called when a new DatabaseHelper is made, and what it does is it
     * creates a new Table with the Constants that we specified as the Table Name and its Columns
     *
     * @param db The SQLite Database
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /* For the Primary Key AUTOINCREMENT for the First Column to add 1 to the Primary Key Value
         * as a Row is added. The execSQL Method simply executes this query on the Database
         */
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME
                + "("
                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL2 + " TEXT,"
                + COL3 + " TEXT,"
                + COL4 + " TEXT,"
                + COL5 + " TEXT,"
                + COL6 + " INTEGER,"
                + COL7 + " TEXT,"
                + COL8 + " INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * This Method is used add a Movie to the Database
     *
     * @param movieToAdd The Movie Object that contains the Details to be added to the Database
     */
    public void addData(Movie movieToAdd)
    {
        /* Creating a DatabaseHelper Instance to access the getData Method in order to get the
         * Title (Index 1 in terms of Columns) of all the saved Movies in the Database and to make
         * sure that the Title of the Movie to be registered isn't equals to any of the existing movies
         */
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor c = databaseHelper.getData();

        ArrayList<String> listLabels = new ArrayList<>();

        while(c.moveToNext())
        {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listLabels.add(c.getString(1));
        }

        // Error Message if the Title of the Movie to Add Already is found
        if (listLabels.contains(movieToAdd.getTitle()))
        {
            Toast.makeText(context,"Movie Already has been Registered", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int favorite = 0;

            if (movieToAdd.isFavorite())

                favorite = 1;

            // e execSQL Method simply executes this query on the Database
            this.getWritableDatabase().execSQL(
                    "INSERT INTO " + TABLE_NAME +
                    " (title, year, director, actor_actress, rating, review, favorite)" +
                    " values('"
                    + movieToAdd.getTitle() + "', '" + movieToAdd.getYear() + "', '"
                    + movieToAdd.getDirector() + "', '" +  movieToAdd.getActorActress() + "', "
                    + movieToAdd.getRating() + ", '" + movieToAdd.getReview() + "', " +  favorite
                    + ")");

            Toast.makeText(context, "Movie Registered Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This Method returns a Cursor containing Rows for all Columns in the Database
     *
     * @return Cursor containing Data for All Movies
     */
    public Cursor getData()
    {
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    /**
     * This Method returns a Cursor containing Rows for all Columns in the Database that are similar
     * to the Data that is to be Filtered
     *
     * @param filterData The Data to be Filtered
     *
     * @return Cursor containing data of Filtered Movies
     */
    public Cursor getSearchedData(String filterData)
    {
        filterData = filterData.toUpperCase();

        return this.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                " WHERE upper(" + COL2 + ") LIKE '%" + filterData + "%'" +
                " OR upper(" + COL4 + ") LIKE '%" + filterData + "%'" +
                " OR upper(" + COL5 + ") LIKE '%" + filterData + "%'", null);
    }

    /**
     * This Method replaces the Integer that is used as a Boolean of the Favorite Column for a Row
     * based on its Title
     *
     * @param title The Title of the Movie
     * @param favorite Integer that determines if the Movie is favorite or not
     */
    public void makeFavorite(String title, int favorite)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // e execSQL Method simply executes this query on the Database
        db.execSQL(
                "UPDATE " + TABLE_NAME +
                " SET " + COL8 + " = " + favorite +
                " WHERE " + COL2 + " = '" + title + "'");
    }

    /**
     * This Method is used to Replace an entire Row with the Values given from a Movie Object
     *
     * @param id The ID of the Movie we want to Replace
     * @param movie The Movie Object that we want to get Values from
     */
    public void editMovie(int id, Movie movie)
    {
        // Since SQLite does not support Booleans, an Integer is used with 0 as false and 1 as true
        int favorite = 0;

        if (movie.isFavorite())

            favorite = 1;

        // e execSQL Method simply executes this query on the Database
        this.getWritableDatabase().execSQL(
                "REPLACE INTO " + TABLE_NAME +
                " (movieId, title, year, director, actor_actress, rating, review, favorite)" +
                " Values(" + id + ", '" + movie.getTitle() + "', '" + movie.getYear() + "', '" + movie.getDirector() +
                "', '" + movie.getActorActress() + "', " + movie.getRating() + ", '" + movie.getReview() +
                "', " + favorite + ")");
    }
}
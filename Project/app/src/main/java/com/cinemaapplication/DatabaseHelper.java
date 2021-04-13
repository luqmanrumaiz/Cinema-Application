package com.cinemaapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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
    public void addData(Movie movieToAdd, View view)
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

            Snackbar.make(view, "Movie Already has already been Registered !!!",Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(context.getResources().getColor(R.color.transparent_yellow))
                    .setTextColor(context.getResources().getColor(R.color.grey_black))
                    .show();

        else
        {
            int favorite = 0;

            if (movieToAdd.isFavorite())

                favorite = 1;

            /* the execSQL Method simply executes this query on the Database, in this Query UPDATE is
             * used to specify the Table Name to Update, SET specifies the Column name to Update in this
             * case COL8 contains favorites and WHERE is the condition in this case it is to make sure
             * that the Movie Title Matches with the given title
             */
            this.getWritableDatabase().execSQL(
                    "INSERT INTO " + TABLE_NAME +
                    " (title, year, director, actor_actress, rating, review, favorite)" +
                    " values('"
                    + movieToAdd.getTitle() + "', '" + movieToAdd.getYear() + "', '"
                    + movieToAdd.getDirector() + "', '" +  movieToAdd.getActorActress() + "', "
                    + movieToAdd.getRating() + ", '" + movieToAdd.getReview() + "', " +  favorite
                    + ")");

            Snackbar.make(view, "Successfully Registered !",Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(context.getResources().getColor(R.color.transparent_yellow))
                    .setTextColor(context.getResources().getColor(R.color.grey_black))
                    .show();
        }
    }

    /**
     * This Method returns a Cursor containing Rows for all Columns in the Database
     *
     * @return Cursor containing Data for All Movies
     */
    public Cursor getData()
    {
        /* We are using the rawQuery method to execute the SQLite Query we have made and return
         * the Data in the form of a Cursor. As for the Query itself SELECT specifies what Columns
         * we want to select in this case its everything (*)
         */
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    /**
     * This Method returns a Cursor containing Rows for all Columns in the Database sorted
     * in Alphabetical Order
     *
     * @return Cursor containing Data for All Movies
     */
    public Cursor getSortedData()
    {
        /* We are using the rawQuery method to execute the SQLite Query we have made and return
         * the Data in the form of a Cursor. As for the Query itself SELECT specifies what Columns
         * we want to select in this case its everything (*) and ORDER BY sorts the Data in this case
         * we require Data to be sorted in alphabetical order so we use ASC
         * */
        return this.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " ORDER BY " + COL2 + " ASC", null);
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

        /* We are using the rawQuery method to execute the SQLite Query we have made and return
         * the Data in the form of a Cursor. As for the Query itself SELECT specifies what Columns
         * we want to select in this case its everything (*), WHERE specifies the Condition
         * we place in this case we First check COL2 which contains the Movie Titles and Use Upper
         * to make it Upper Case and we use LIKE to see if the Movie Title from COL2 contains the
         * data that we are filtering, this same condition applies for COL4 and COl5 and by
         * using OR we can fulfil the WHERE Condition if any of these Conditions are met
         * */
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

        /* the execSQL Method simply executes this query on the Database, in this Query UPDATE is
         * used to specify the Table Name to Update, SET specifies the Column name to Update in this
         * case COL8 contains favorites and WHERE is the condition in this case it is to make sure
         * that the Movie Title Matches with the given title
         */
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

        /* the execSQL Method simply executes this query on the Database, in this Query REPLACE INTO
         * replaces a Row in the table based on the movieId value specified
         */
        this.getWritableDatabase().execSQL(
                "REPLACE INTO " + TABLE_NAME +
                " (movieId, title, year, director, actor_actress, rating, review, favorite)" +
                " Values(" + id + ", '" + movie.getTitle() + "', '" + movie.getYear() + "', '" + movie.getDirector() +
                "', '" + movie.getActorActress() + "', " + movie.getRating() + ", '" + movie.getReview() +
                "', " + favorite + ")");
    }
}
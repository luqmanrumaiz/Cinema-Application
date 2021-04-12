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

    private Context context;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = "CREATE TABLE " + TABLE_NAME
                + "("
                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL2 + " TEXT,"
                + COL3 + " TEXT,"
                + COL4 + " TEXT,"
                + COL5 + " TEXT,"
                + COL6 + " INTEGER,"
                + COL7 + " TEXT,"
                + COL8 + " INTEGER" +
                ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addData(Movie movieToAdd)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor c = databaseHelper.getData();

        ArrayList<String> listLabels = new ArrayList<>();

        while(c.moveToNext())
        {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listLabels.add(c.getString(1));
        }

        if (listLabels.contains(movieToAdd.getTitle()))
        {
            Toast.makeText(context,"Movie Already has been Registered", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int favorite = 0;

            if (movieToAdd.isFavorite())

                favorite = 1;


            db.execSQL("INSERT INTO " + TABLE_NAME +
                    " (title, year, director, actor_actress, rating, review, favorite)" +
                    " values('"
                    + movieToAdd.getTitle() + "', '" + movieToAdd.getYear() + "', '"
                    + movieToAdd.getDirector() + "', '" +  movieToAdd.getActorActress() + "', "
                    + movieToAdd.getRating() + ", '" + movieToAdd.getReview() + "', " +  favorite
                    + ")");

            Toast.makeText(context, "Movie Registered Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public Cursor getSearchedData(String filterData)
    {
        filterData = filterData.toUpperCase();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE upper(" + COL2 + ") LIKE '%" + filterData + "%'" +
                " OR upper(" + COL4 + ") LIKE '%" + filterData + "%'" +
                " OR upper(" + COL5 + ") LIKE '%" + filterData + "%'";

        return db.rawQuery(query, null);
    }

    public void makeFavorite(int id, String title, int favorite)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME +
                " SET " + COL8 + " = " + favorite +
                " WHERE " + COL2 + " = '" + title + "'");
    }



    public void editMovie(int id, Movie movie)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int favorite = 0;

        if (movie.isFavorite())

            favorite = 1;

        db.execSQL("REPLACE INTO " + TABLE_NAME +
                " (movieId, title, year, director, actor_actress, rating, review, favorite)" +
                " Values(" + id + ", '" + movie.getTitle() + "', '" + movie.getYear() + "', '" + movie.getDirector() +
                "', '" + movie.getActorActress() + "', " + movie.getRating() + ", '" + movie.getReview() +
                "', " + favorite + ")");
    }
}
package com.cinemaapplication;

import java.util.ArrayList;
import java.util.List;

public class Movie
{
    private String title;
    private String year;
    private String review;
    private String director;
    private String actorActress;
    private int rating;
    private boolean favorite = false;

    public Movie(String title, String year, String director, String actorActress,
                 int rating, String review, boolean favorite)
    {
        this.title = title;
        this.year = year;
        this.review = review;
        this.director = director;
        this.actorActress = actorActress;
        this.rating = rating;
        this.favorite = favorite;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getReview()
    {
        return review;
    }

    public void setReview(String review)
    {
        this.review = review;
    }

    public String getDirector()
    {
        return director;
    }

    public void setDirector(String director)
    {
        this.director = director;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public String getActorActress() {
        return actorActress;
    }

    public void setActorActress(String actorActress)
    {
        this.actorActress = actorActress;
    }

    public boolean isFavorite()
    {
        return favorite;
    }

    public void setFavorite(boolean favorite)
    {
        this.favorite = favorite;
    }
}


package com.cinemaapplication;

import java.util.ArrayList;
import java.util.List;

public class Movie
{
    private String title;
    private String date;
    private String review;
    private String director;
    private List<String> actorActressList;
    private int rating;
    private boolean favorite = false;

    public Movie(String title, String date, String review, String director, int rating, boolean favorite)
    {
        this.title = title;
        this.date = date;
        this.review = review;
        this.director = director;
        this.rating = rating;
        this.favorite = favorite;
        actorActressList = new ArrayList<>();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
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

    public List<String> getActorActressList()
    {
        return actorActressList;
    }

    public void setActorActressList(List<String> actorActressList)
    {
        this.actorActressList = actorActressList;
    }

    public boolean isFavorite()
    {
        return favorite;
    }

    public void setFavorite(boolean favorite)
    {
        this.favorite = favorite;
    }

    public void addActorOrActress(String actorOrActressName)
    {
        actorActressList.add(actorOrActressName);
    }

    @Override
    public String toString()
    {
        return "Movie{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", review='" + review + '\'' +
                ", director='" + director + '\'' +
                ", actorActressList=" + actorActressList +
                ", rating=" + rating +
                ", favorite=" + favorite +
                '}';
    }
}


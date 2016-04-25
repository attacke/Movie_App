package com.example.omarhamdy.pro_phase1;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Omar Hamdy on 25/03/2016.
 */
public class myMovieData extends RealmObject{

    @PrimaryKey
    private String id ;
    private String  original_title, poster, overview, release_date, vote_average;

    public myMovieData() {
    }

    myMovieData(String id, String original_title, String poster, String overview, String release_date, String vote_average) {

        this.id = id;
        this.original_title = original_title;
        this.poster = poster;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {return original_title;}

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {return release_date;}

    public void setRelease_date(String release_date) {this.release_date = release_date;}

    public String getVote_average() {return vote_average;}

    public void setVote_average(String vote_average) {this.vote_average = vote_average;}
}
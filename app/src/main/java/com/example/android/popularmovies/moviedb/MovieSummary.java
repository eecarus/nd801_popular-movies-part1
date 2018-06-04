package com.example.android.popularmovies.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;

public class MovieSummary implements Parcelable {

    private Integer voteCount;
    private Long id;
    private boolean video;
    private String voteAverage;
    private String title;
    private String popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private Integer[] genreIds = new Integer[0];
    private String backdropPath;
    private Boolean adult;
    private String overview;
    private LocalDate releaseDate;

    public MovieSummary() {
        // no-arg constructor
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Integer[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(Integer[] genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    // ---------------------------------------------------------------------------------------------
    // Implementation of Parcelable
    // ---------------------------------------------------------------------------------------------

    private MovieSummary(Parcel in) {
        voteCount = in.readInt();
        id = in.readLong();
        video = in.readInt() == 1;
        voteAverage = in.readString();
        title = in.readString();
        popularity = in.readString();
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        adult = in.readInt() == 1;
        overview = in.readString();
        releaseDate = (LocalDate) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(voteCount);
        dest.writeLong(id);
        dest.writeInt(video ? 1 : 0);
        dest.writeString(voteAverage);
        dest.writeString(title);
        dest.writeString(popularity);
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(backdropPath);
        dest.writeInt(adult ? 1 : 0);
        dest.writeString(overview);
        dest.writeSerializable(releaseDate);
    }

    public static final Parcelable.Creator<MovieSummary> CREATOR = new Parcelable.Creator<MovieSummary>() {

        public MovieSummary createFromParcel(Parcel in) {
            return new MovieSummary(in);
        }

        public MovieSummary[] newArray(int size) {
            return new MovieSummary[size];
        }
    };

}
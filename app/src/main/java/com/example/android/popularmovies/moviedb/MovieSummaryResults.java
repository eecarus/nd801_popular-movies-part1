package com.example.android.popularmovies.moviedb;

public class MovieSummaryResults {
    private Integer page;
    private Long totalResults;
    private Long totalPages;
    private MovieSummary[] results = new MovieSummary[0];

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public MovieSummary[] getResults() {
        return results;
    }

    public void setResults(MovieSummary[] results) {
        this.results = results;
    }
}

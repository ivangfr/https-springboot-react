package com.ivanfranchin.moviesapi.movie.dto;

import com.ivanfranchin.moviesapi.movie.model.Movie;

public record MovieResponse(String imdbId, String title, String director, Integer year) {

    public static MovieResponse from(Movie movie) {
        return new MovieResponse(
                movie.getImdbId(),
                movie.getTitle(),
                movie.getDirector(),
                Integer.valueOf(movie.getYear())
        );
    }
}
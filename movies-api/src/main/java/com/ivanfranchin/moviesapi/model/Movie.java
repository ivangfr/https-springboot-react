package com.ivanfranchin.moviesapi.model;

import com.ivanfranchin.moviesapi.rest.dto.AddMovieRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Movie {

    @Id
    private String imdbId;
    private String title;
    private String director;
    private String year;

    public static Movie from(AddMovieRequest addMovieRequest) {
        Movie movie = new Movie();
        movie.setImdbId(addMovieRequest.imdbId());
        movie.setTitle(addMovieRequest.title());
        movie.setDirector(addMovieRequest.director());
        movie.setYear(String.valueOf(addMovieRequest.year()));
        return movie;
    }
}
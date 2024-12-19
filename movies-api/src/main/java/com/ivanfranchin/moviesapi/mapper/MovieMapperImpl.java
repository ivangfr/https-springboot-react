package com.ivanfranchin.moviesapi.mapper;

import com.ivanfranchin.moviesapi.model.Movie;
import com.ivanfranchin.moviesapi.rest.dto.AddMovieRequest;
import com.ivanfranchin.moviesapi.rest.dto.MovieResponse;
import org.springframework.stereotype.Service;

@Service
public class MovieMapperImpl implements MovieMapper {

    @Override
    public MovieResponse toMovieResponse(Movie movie) {
        if (movie == null) {
            return null;
        }
        return new MovieResponse(movie.getImdbId(), movie.getTitle(), movie.getDirector(), Integer.valueOf(movie.getYear()));
    }

    @Override
    public Movie toMovie(AddMovieRequest addMovieRequest) {
        if (addMovieRequest == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setImdbId(addMovieRequest.imdbId());
        movie.setTitle(addMovieRequest.title());
        movie.setDirector(addMovieRequest.director());
        movie.setYear(String.valueOf(addMovieRequest.year()));
        return movie;
    }
}

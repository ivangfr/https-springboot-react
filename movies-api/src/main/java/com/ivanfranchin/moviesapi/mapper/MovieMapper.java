package com.ivanfranchin.moviesapi.mapper;

import com.ivanfranchin.moviesapi.model.Movie;
import com.ivanfranchin.moviesapi.rest.dto.AddMovieRequest;
import com.ivanfranchin.moviesapi.rest.dto.MovieResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieResponse toMovieResponse(Movie movie);

    Movie toMovie(AddMovieRequest addMovieRequest);
}
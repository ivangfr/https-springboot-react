package com.mycompany.moviesapi.mapper;

import com.mycompany.moviesapi.model.Movie;
import com.mycompany.moviesapi.rest.dto.AddMovieRequest;
import com.mycompany.moviesapi.rest.dto.MovieResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieResponse toMovieResponse(Movie movie);

    Movie toMovie(AddMovieRequest addMovieRequest);
}
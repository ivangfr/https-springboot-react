package com.ivanfranchin.moviesapi.rest;

import com.ivanfranchin.moviesapi.mapper.MovieMapper;
import com.ivanfranchin.moviesapi.model.Movie;
import com.ivanfranchin.moviesapi.rest.dto.AddMovieRequest;
import com.ivanfranchin.moviesapi.rest.dto.MovieResponse;
import com.ivanfranchin.moviesapi.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @GetMapping
    public List<MovieResponse> getMovies() {
        return movieService.getMovies().stream().map(movieMapper::toMovieResponse).collect(Collectors.toList());
    }

    @GetMapping("/{imdbId}")
    public MovieResponse getMovie(@PathVariable String imdbId) {
        Movie movie = movieService.validateAndGetMovie(imdbId);
        return movieMapper.toMovieResponse(movie);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieResponse addMovie(@Valid @RequestBody AddMovieRequest addMovieRequest) {
        Movie movie = movieMapper.toMovie(addMovieRequest);
        movie = movieService.saveMovie(movie);
        return movieMapper.toMovieResponse(movie);
    }

    @DeleteMapping("/{imdbId}")
    public MovieResponse deleteMovie(@PathVariable String imdbId) {
        Movie movie = movieService.validateAndGetMovie(imdbId);
        movieService.deleteMovie(movie);
        return movieMapper.toMovieResponse(movie);
    }
}
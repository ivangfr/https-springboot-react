package com.ivanfranchin.moviesapi.movie;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ivanfranchin.moviesapi.movie.dto.AddMovieRequest;
import com.ivanfranchin.moviesapi.movie.dto.MovieResponse;
import com.ivanfranchin.moviesapi.movie.model.Movie;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MoviesController {

  private final MovieService movieService;

  @GetMapping
  public List<MovieResponse> getMovies() {
    return movieService.getMovies().stream().map(MovieResponse::from).toList();
  }

  @GetMapping("/{imdbId}")
  public MovieResponse getMovie(@PathVariable String imdbId) {
    Movie movie = movieService.validateAndGetMovie(imdbId);
    return MovieResponse.from(movie);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public MovieResponse addMovie(@Valid @RequestBody AddMovieRequest addMovieRequest) {
    Movie movie = Movie.from(addMovieRequest);
    movie = movieService.saveMovie(movie);
    return MovieResponse.from(movie);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{imdbId}")
  public void deleteMovie(@PathVariable String imdbId) {
    Movie movie = movieService.validateAndGetMovie(imdbId);
    movieService.deleteMovie(movie);
  }
}

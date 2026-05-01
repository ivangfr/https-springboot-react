package com.ivanfranchin.moviesshell.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.ivanfranchin.moviesshell.dto.AddMovieRequest;
import com.ivanfranchin.moviesshell.dto.MovieResponse;

@HttpExchange("/api/movies")
public interface MoviesApiClient {

  @GetExchange
  ResponseEntity<List<MovieResponse>> getMovies();

  @GetExchange("/{imdbId}")
  ResponseEntity<MovieResponse> getMovie(@PathVariable String imdbId);

  @PostExchange
  ResponseEntity<MovieResponse> addMovie(@RequestBody AddMovieRequest addMovieRequest);

  @DeleteExchange("/{imdbId}")
  ResponseEntity<Void> deleteMovie(@PathVariable String imdbId);
}

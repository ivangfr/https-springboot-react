package com.ivanfranchin.moviesshell.command;

import com.ivanfranchin.moviesshell.client.MovieApiClient;
import com.ivanfranchin.moviesshell.dto.AddMovieRequest;
import com.ivanfranchin.moviesshell.dto.MovieResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoviesCommands {

    private final MovieApiClient movieApiClient;

    public MoviesCommands(MovieApiClient movieApiClient) {
        this.movieApiClient = movieApiClient;
    }

    @Value("${movies-api.url}")
    private String movieApiUrl;

    @Command(name = "get-movies", description = "Get all movies", group = "Movies API")
    public String getMovies() {
        try {
            ResponseEntity<List<MovieResponse>> response = movieApiClient.getMovies();
            return String.format("%s %s", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Command(name = "get-movie", description = "Get specific movie", group = "Movies API")
    public String getMovie(@Option(longName = "imdbId", required = true) String imdbId) {
        try {
            ResponseEntity<MovieResponse> response = movieApiClient.getMovie(imdbId);
            return String.format("%s %s", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Command(name = "add-movies", description = "Add movie", group = "Movies API")
    public String addMovie(@Option(longName = "imdbId", required = true) String imdbId,
                           @Option(longName = "title", required = true) String title,
                           @Option(longName = "director", required = true) String director,
                           @Option(longName = "year", required = true) int year) {
        try {
            ResponseEntity<MovieResponse> response = movieApiClient.addMovie(new AddMovieRequest(imdbId, title, director, year));
            return String.format("%s %s", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Command(name = "delete-movies", description = "Delete movie", group = "Movies API")
    public String deleteMovie(@Option(longName = "imdbId", required = true) String imdbId) {
        try {
            ResponseEntity<MovieResponse> response = movieApiClient.deleteMovie(imdbId);
            return String.format("%s %s", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
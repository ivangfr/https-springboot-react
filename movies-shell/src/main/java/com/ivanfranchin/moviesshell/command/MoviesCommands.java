package com.ivanfranchin.moviesshell.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivanfranchin.moviesshell.client.MovieApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MoviesCommands {

    private final MovieApiClient movieApiClient;

    public MoviesCommands(MovieApiClient movieApiClient) {
        this.movieApiClient = movieApiClient;
    }

    @Value("${movies-api.url}")
    private String movieApiUrl;

    @ShellMethod("Get all movies")
    public String getMovies() {
        ResponseEntity<String> response = movieApiClient.getMovies();
        return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
    }

    @ShellMethod("Get specific movie")
    public String getMovie(String imdbId) {
        ResponseEntity<String> response = movieApiClient.getMovie(imdbId);
        return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
    }

    @ShellMethod("Add movie")
    public String addMovie(String imdbId, String title, String director, int year) throws JsonProcessingException {
        ResponseEntity<String> response = movieApiClient.addMovie(imdbId, title, director, year);
        return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
    }

    @ShellMethod("Delete movie")
    public String deleteMovie(String imdbId) {
        ResponseEntity<String> response = movieApiClient.deleteMovie(imdbId);
        return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
    }
}
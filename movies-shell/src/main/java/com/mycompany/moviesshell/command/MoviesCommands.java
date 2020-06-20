package com.mycompany.moviesshell.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.moviesshell.client.MovieApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class MoviesCommands {

    @Value("${movies-api.url}")
    private String movieApiUrl;

    private final MovieApiClient movieApiClient;

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

    @ShellMethod("Create movie")
    public String createMovie(String imdbId, String title, String director, int year) throws JsonProcessingException {
        ResponseEntity<String> response = movieApiClient.createMovie(imdbId, title, director, year);
        return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
    }

    @ShellMethod("Delete movie")
    public String deleteMovie(String imdbId) {
        ResponseEntity<String> response = movieApiClient.deleteMovie(imdbId);
        return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
    }

}
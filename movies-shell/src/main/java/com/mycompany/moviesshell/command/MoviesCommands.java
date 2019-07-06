package com.mycompany.moviesshell.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.moviesshell.dto.CreateMovieDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class MoviesCommands {

  @Value("${movies-api.url}")
  private String movieApiUrl;

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public MoviesCommands(RestTemplate restTemplate, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  @ShellMethod("Get all movies")
  public String getMovies() {
    ResponseEntity<String> response = restTemplate.getForEntity(movieApiUrl, String.class);
    return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
  }

  @ShellMethod("Get specific movie")
  public String getMovie(String imdbId) {
    String url = String.format("%s/%s", movieApiUrl, imdbId);
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
  }

  @ShellMethod("Create movie")
  public String createMovie(String imdbId, String title, String director, int year) throws JsonProcessingException {
    CreateMovieDto createMovieDto = new CreateMovieDto(imdbId, title, director, year);
    String createMovieDtoStr = objectMapper.writeValueAsString(createMovieDto);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<>(createMovieDtoStr, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(movieApiUrl, request, String.class);
    return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
  }

  @ShellMethod("Delete movie")
  public String deleteMovie(String imdbId) {
    String url = String.format("%s/%s", movieApiUrl, imdbId);
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    return String.format("%s %s", response.getStatusCodeValue(), response.getBody());
  }

}
package com.mycompany.moviesclient.rest;

import java.util.List;

import javax.validation.Valid;

import com.mycompany.moviesclient.rest.dto.CreateMovieDto;
import com.mycompany.moviesclient.rest.dto.MovieDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/movies")
public class MoviesController {

  @Value("${movies-api.url}")
  private String movieApiUrl;

  private final RestTemplate restTemplate;

  public MoviesController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public List<MovieDto> getMovies() {
    ResponseEntity<List<MovieDto>> response = restTemplate.exchange(movieApiUrl, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<MovieDto>>() {
        });
    log.info("MoviesApi - getMovies - Response: {}", response.getStatusCode());
    return response.getBody();
  }

  @GetMapping("/{imdbId}")
  public MovieDto getMovie(@PathVariable String imdbId) {
    String url = String.format("%s/%s", movieApiUrl, imdbId);
    ResponseEntity<MovieDto> response = restTemplate.exchange(url, HttpMethod.GET, null, MovieDto.class);
    log.info("MoviesApi - getMovie - Response: {}", response.getStatusCode());
    return response.getBody();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public MovieDto createMovie(@Valid @RequestBody CreateMovieDto createMovieDto) {
    HttpEntity<CreateMovieDto> request = new HttpEntity<>(createMovieDto);
    ResponseEntity<MovieDto> response = restTemplate.exchange(movieApiUrl, HttpMethod.POST, request, MovieDto.class);
    log.info("MoviesApi - createMovie - Response: {}", response.getStatusCode());
    return response.getBody();
  }

  @DeleteMapping("/{imdbId}")
  public MovieDto deleteMovie(@PathVariable String imdbId) {
    String url = String.format("%s/%s", movieApiUrl, imdbId);
    ResponseEntity<MovieDto> response = restTemplate.exchange(url, HttpMethod.DELETE, null, MovieDto.class);
    log.info("MoviesApi - deleteMovie - Response: {}", response.getStatusCode());
    return response.getBody();
  }

}
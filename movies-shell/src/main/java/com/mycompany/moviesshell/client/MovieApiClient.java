package com.mycompany.moviesshell.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.moviesshell.dto.CreateMovieDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class MovieApiClient {

    @Value("${movies-api.url}")
    private String movieApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ResponseEntity<String> getMovies() {
        return restTemplate.getForEntity(movieApiUrl, String.class);
    }

    public ResponseEntity<String> getMovie(String imdbId) {
        String url = String.format("%s/%s", movieApiUrl, imdbId);
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> createMovie(String imdbId, String title, String director, int year) throws JsonProcessingException {
        CreateMovieDto createMovieDto = new CreateMovieDto(imdbId, title, director, year);
        String createMovieDtoStr = objectMapper.writeValueAsString(createMovieDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(createMovieDtoStr, headers);
        return restTemplate.postForEntity(movieApiUrl, request, String.class);
    }

    public ResponseEntity<String> deleteMovie(String imdbId) {
        String url = String.format("%s/%s", movieApiUrl, imdbId);
        return restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }

}
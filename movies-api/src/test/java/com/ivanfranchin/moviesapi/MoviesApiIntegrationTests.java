package com.ivanfranchin.moviesapi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ivanfranchin.moviesapi.movie.MovieRepository;
import com.ivanfranchin.moviesapi.movie.dto.AddMovieRequest;
import com.ivanfranchin.moviesapi.movie.dto.MovieResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class MoviesApiIntegrationTests {

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private MovieRepository movieRepository;

  private static final String MOVIES_URL = "/api/movies";

  @BeforeEach
  void cleanDatabase() {
    movieRepository.deleteAll();
  }

  // -- helpers --

  private MovieResponse addMovie(String imdbId, String title, String director, int year) {
    AddMovieRequest request = new AddMovieRequest(imdbId, title, director, year);
    ResponseEntity<MovieResponse> response =
        restTemplate.postForEntity(MOVIES_URL, request, MovieResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    return response.getBody();
  }

  // -- GET /api/movies --

  @Test
  public void testGetMoviesReturnsEmptyListWhenNoMoviesExist() {
    ResponseEntity<MovieResponse[]> response =
        restTemplate.getForEntity(MOVIES_URL, MovieResponse[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEmpty();
  }

  @Test
  public void testGetMoviesReturnsPersistedMovies() {
    addMovie("tt0120804", "Resident Evil", "Paul W.S. Anderson", 2002);
    addMovie("tt0119698", "Princess Mononoke", "Hayao Miyazaki", 1997);

    ResponseEntity<MovieResponse[]> response =
        restTemplate.getForEntity(MOVIES_URL, MovieResponse[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(2);
  }

  // -- GET /api/movies/{imdbId} --

  @Test
  public void testGetMovieReturnsCorrectMovieWhenFound() {
    addMovie("tt0120804", "Resident Evil", "Paul W.S. Anderson", 2002);

    ResponseEntity<MovieResponse> response =
        restTemplate.getForEntity(MOVIES_URL + "/tt0120804", MovieResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    MovieResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.imdbId()).isEqualTo("tt0120804");
    assertThat(body.title()).isEqualTo("Resident Evil");
    assertThat(body.director()).isEqualTo("Paul W.S. Anderson");
    assertThat(body.year()).isEqualTo(2002);
  }

  @Test
  public void testGetMovieReturns404WhenNotFound() {
    ResponseEntity<String> response =
        restTemplate.getForEntity(MOVIES_URL + "/tt9999999", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  // -- POST /api/movies --

  @Test
  public void testAddMovieReturns201WithCreatedMovieBody() {
    AddMovieRequest request =
        new AddMovieRequest("tt0120804", "Resident Evil", "Paul W.S. Anderson", 2002);

    ResponseEntity<MovieResponse> response =
        restTemplate.postForEntity(MOVIES_URL, request, MovieResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    MovieResponse body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.imdbId()).isEqualTo("tt0120804");
    assertThat(body.title()).isEqualTo("Resident Evil");
  }

  @Test
  public void testAddMovieReturns400WhenImdbIdIsBlank() {
    AddMovieRequest request = new AddMovieRequest("", "Resident Evil", "Paul W.S. Anderson", 2002);

    ResponseEntity<String> response = restTemplate.postForEntity(MOVIES_URL, request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testAddMovieReturns400WhenYearIsNegative() {
    AddMovieRequest request =
        new AddMovieRequest("tt0120804", "Resident Evil", "Paul W.S. Anderson", -1);

    ResponseEntity<String> response = restTemplate.postForEntity(MOVIES_URL, request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  // -- DELETE /api/movies/{imdbId} --

  @Test
  public void testDeleteMovieReturns204AndRemovesIt() {
    addMovie("tt0120804", "Resident Evil", "Paul W.S. Anderson", 2002);

    ResponseEntity<Void> deleteResponse =
        restTemplate.exchange(
            MOVIES_URL + "/tt0120804", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

    assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    // Verify movie is gone
    ResponseEntity<String> getResponse =
        restTemplate.getForEntity(MOVIES_URL + "/tt0120804", String.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testDeleteMovieReturns404WhenNotFound() {
    ResponseEntity<String> response =
        restTemplate.exchange(
            MOVIES_URL + "/tt9999999", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}

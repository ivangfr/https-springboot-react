package com.ivanfranchin.moviesapi.movie;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.ivanfranchin.moviesapi.movie.dto.AddMovieRequest;
import com.ivanfranchin.moviesapi.movie.model.Movie;

@DataJpaTest
class MovieRepositoryTests {

  @Autowired private TestEntityManager entityManager;

  @Autowired private MovieRepository movieRepository;

  // -- helpers --

  private Movie persistMovie(String imdbId) {
    Movie movie =
        Movie.from(new AddMovieRequest(imdbId, "Resident Evil", "Paul W.S. Anderson", 2002));
    return entityManager.persistAndFlush(movie);
  }

  // -- findById --

  @Test
  public void testFindByIdReturnsMovieWhenExists() {
    persistMovie("tt0120804");

    Optional<Movie> result = movieRepository.findById("tt0120804");

    assertThat(result).isPresent();
    assertThat(result.get().getImdbId()).isEqualTo("tt0120804");
    assertThat(result.get().getTitle()).isEqualTo("Resident Evil");
    assertThat(result.get().getDirector()).isEqualTo("Paul W.S. Anderson");
    assertThat(result.get().getYear()).isEqualTo(2002);
  }

  @Test
  public void testFindByIdReturnsEmptyWhenNotExists() {
    Optional<Movie> result = movieRepository.findById("tt9999999");

    assertThat(result).isEmpty();
  }

  // -- save --

  @Test
  public void testSavePersistsMovieWithAllFields() {
    Movie movie =
        Movie.from(new AddMovieRequest("tt0120804", "Resident Evil", "Paul W.S. Anderson", 2002));

    Movie saved = movieRepository.save(movie);
    entityManager.flush();
    entityManager.detach(saved);

    Movie found = entityManager.find(Movie.class, "tt0120804");
    assertThat(found).isNotNull();
    assertThat(found.getTitle()).isEqualTo("Resident Evil");
    assertThat(found.getDirector()).isEqualTo("Paul W.S. Anderson");
    assertThat(found.getYear()).isEqualTo(2002);
  }

  // -- delete --

  @Test
  public void testDeleteRemovesMovieFromDatabase() {
    Movie movie = persistMovie("tt0120804");

    movieRepository.delete(movie);
    entityManager.flush();

    Movie found = entityManager.find(Movie.class, "tt0120804");
    assertThat(found).isNull();
  }

  // -- findAll --

  @Test
  public void testFindAllReturnsEmptyListWhenNoMoviesExist() {
    List<Movie> result = movieRepository.findAll();

    assertThat(result).isEmpty();
  }

  @Test
  public void testFindAllReturnsAllPersistedMovies() {
    persistMovie("tt0120804");
    persistMovie("tt0119698");

    List<Movie> result = movieRepository.findAll();

    assertThat(result).hasSize(2);
    assertThat(result).extracting("imdbId").containsExactlyInAnyOrder("tt0120804", "tt0119698");
  }
}

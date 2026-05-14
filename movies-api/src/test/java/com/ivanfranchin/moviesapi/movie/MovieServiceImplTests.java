package com.ivanfranchin.moviesapi.movie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ivanfranchin.moviesapi.movie.dto.AddMovieRequest;
import com.ivanfranchin.moviesapi.movie.exception.MovieNotFoundException;
import com.ivanfranchin.moviesapi.movie.model.Movie;

@ExtendWith(SpringExtension.class)
@Import(MovieServiceImpl.class)
class MovieServiceImplTests {

  @MockitoBean private MovieRepository movieRepository;

  @Autowired private MovieServiceImpl movieService;

  // -- helpers --

  private Movie buildMovie(String imdbId) {
    return Movie.from(new AddMovieRequest(imdbId, "Resident Evil", "Paul W.S. Anderson", 2002));
  }

  // -- getMovies --

  @Test
  public void testGetMoviesReturnsEmptyListWhenNoMoviesExist() {
    when(movieRepository.findAll()).thenReturn(List.of());

    List<Movie> result = movieService.getMovies();

    assertThat(result).isEmpty();
    verify(movieRepository).findAll();
    verifyNoMoreInteractions(movieRepository);
  }

  @Test
  public void testGetMoviesReturnsAllMovies() {
    Movie movie1 = buildMovie("tt0120804");
    Movie movie2 = buildMovie("tt0119698");
    when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

    List<Movie> result = movieService.getMovies();

    assertThat(result).hasSize(2).containsExactly(movie1, movie2);
    verify(movieRepository).findAll();
    verifyNoMoreInteractions(movieRepository);
  }

  // -- validateAndGetMovie --

  @Test
  public void testValidateAndGetMovieReturnsMovieWhenFound() {
    Movie movie = buildMovie("tt0120804");
    when(movieRepository.findById("tt0120804")).thenReturn(Optional.of(movie));

    Movie result = movieService.validateAndGetMovie("tt0120804");

    assertThat(result).isEqualTo(movie);
    verify(movieRepository).findById("tt0120804");
    verifyNoMoreInteractions(movieRepository);
  }

  @Test
  public void testValidateAndGetMovieThrowsWhenNotFound() {
    when(movieRepository.findById("tt9999999")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> movieService.validateAndGetMovie("tt9999999"))
        .isInstanceOf(MovieNotFoundException.class)
        .hasMessageContaining("tt9999999");

    verify(movieRepository).findById("tt9999999");
    verifyNoMoreInteractions(movieRepository);
  }

  // -- saveMovie --

  @Test
  public void testSaveMovieDelegatesToRepositoryAndReturnsResult() {
    Movie movie = buildMovie("tt0120804");
    when(movieRepository.save(movie)).thenReturn(movie);

    Movie result = movieService.saveMovie(movie);

    assertThat(result).isEqualTo(movie);
    verify(movieRepository).save(movie);
    verifyNoMoreInteractions(movieRepository);
  }

  // -- deleteMovie --

  @Test
  public void testDeleteMovieDelegatesToRepository() {
    Movie movie = buildMovie("tt0120804");

    movieService.deleteMovie(movie);

    verify(movieRepository).delete(movie);
    verifyNoMoreInteractions(movieRepository);
  }
}

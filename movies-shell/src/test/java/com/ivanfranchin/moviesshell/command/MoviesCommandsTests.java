package com.ivanfranchin.moviesshell.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ivanfranchin.moviesshell.client.MoviesApiClient;
import com.ivanfranchin.moviesshell.dto.AddMovieRequest;
import com.ivanfranchin.moviesshell.dto.MovieResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.core.command.annotation.EnableCommand;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ShellTest
@EnableCommand(MoviesCommands.class)
@Import(MoviesCommands.class)
class MoviesCommandsTests {

  @Autowired ShellTestClient shellTestClient;

  @MockitoBean MoviesApiClient moviesApiClient;

  // ------------------------------------------------------------------
  // get-movies
  // ------------------------------------------------------------------

  @Test
  public void getMovies_whenApiReturnsMovies_thenOutputContainsStatusAndBody() throws Exception {
    MovieResponse movie1 =
        new MovieResponse("tt0111161", "The Shawshank Redemption", "Frank Darabont", 1994);
    MovieResponse movie2 =
        new MovieResponse("tt0068646", "The Godfather", "Francis Ford Coppola", 1972);
    when(moviesApiClient.getMovies()).thenReturn(ResponseEntity.ok(List.of(movie1, movie2)));

    ShellAssertions.assertThat(shellTestClient.sendCommand("get-movies"))
        .containsText("200")
        .containsText("tt0111161")
        .containsText("The Shawshank Redemption")
        .containsText("tt0068646")
        .containsText("The Godfather");
  }

  @Test
  public void getMovies_whenApiReturnsEmptyList_thenOutputContainsStatusAndEmptyList()
      throws Exception {
    when(moviesApiClient.getMovies()).thenReturn(ResponseEntity.ok(List.of()));

    ShellAssertions.assertThat(shellTestClient.sendCommand("get-movies"))
        .containsText("200")
        .containsText("[]");
  }

  @Test
  public void getMovies_whenApiThrowsException_thenOutputContainsErrorMessage() throws Exception {
    when(moviesApiClient.getMovies()).thenThrow(new RuntimeException("Connection refused"));

    ShellAssertions.assertThat(shellTestClient.sendCommand("get-movies"))
        .containsText("Error: Connection refused");
  }

  // ------------------------------------------------------------------
  // get-movie
  // ------------------------------------------------------------------

  @Test
  public void getMovie_whenMovieExists_thenOutputContainsStatusAndMovie() throws Exception {
    MovieResponse movie =
        new MovieResponse("tt0111161", "The Shawshank Redemption", "Frank Darabont", 1994);
    when(moviesApiClient.getMovie("tt0111161")).thenReturn(ResponseEntity.ok(movie));

    ShellAssertions.assertThat(shellTestClient.sendCommand("get-movie --imdbId tt0111161"))
        .containsText("200")
        .containsText("tt0111161")
        .containsText("The Shawshank Redemption")
        .containsText("Frank Darabont")
        .containsText("1994");
  }

  @Test
  public void getMovie_whenMovieDoesNotExist_thenOutputContainsErrorMessage() throws Exception {
    when(moviesApiClient.getMovie("tt9999999")).thenThrow(new RuntimeException("404 Not Found"));

    ShellAssertions.assertThat(shellTestClient.sendCommand("get-movie --imdbId tt9999999"))
        .containsText("Error: 404 Not Found");
  }

  // ------------------------------------------------------------------
  // add-movie
  // ------------------------------------------------------------------

  @Test
  public void addMovie_whenRequestIsValid_thenOutputContainsStatusAndCreatedMovie()
      throws Exception {
    MovieResponse created =
        new MovieResponse("tt0111161", "The Shawshank Redemption", "Frank Darabont", 1994);
    when(moviesApiClient.addMovie(any(AddMovieRequest.class)))
        .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(created));

    ShellAssertions.assertThat(
            shellTestClient.sendCommand(
                "add-movie --imdbId tt0111161 --title \"The Shawshank Redemption\" --director \"Frank Darabont\" --year 1994"))
        .containsText("201")
        .containsText("tt0111161")
        .containsText("The Shawshank Redemption")
        .containsText("Frank Darabont")
        .containsText("1994");
  }

  @Test
  public void addMovie_whenApiThrowsException_thenOutputContainsErrorMessage() throws Exception {
    when(moviesApiClient.addMovie(any(AddMovieRequest.class)))
        .thenThrow(new RuntimeException("400 Bad Request"));

    ShellAssertions.assertThat(
            shellTestClient.sendCommand(
                "add-movie --imdbId tt0111161 --title \"The Shawshank Redemption\" --director \"Frank Darabont\" --year 1994"))
        .containsText("Error: 400 Bad Request");
  }

  // ------------------------------------------------------------------
  // delete-movie
  // ------------------------------------------------------------------

  @Test
  public void deleteMovie_whenMovieExists_thenOutputContains204() throws Exception {
    when(moviesApiClient.deleteMovie("tt0111161")).thenReturn(ResponseEntity.noContent().build());

    ShellAssertions.assertThat(shellTestClient.sendCommand("delete-movie --imdbId tt0111161"))
        .containsText("204");
  }

  @Test
  public void deleteMovie_whenMovieDoesNotExist_thenOutputContainsErrorMessage() throws Exception {
    when(moviesApiClient.deleteMovie("tt9999999")).thenThrow(new RuntimeException("404 Not Found"));

    ShellAssertions.assertThat(shellTestClient.sendCommand("delete-movie --imdbId tt9999999"))
        .containsText("Error: 404 Not Found");
  }
}

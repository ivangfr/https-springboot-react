package com.ivanfranchin.moviesapi.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.moviesapi.movie.dto.AddMovieRequest;
import com.ivanfranchin.moviesapi.movie.exception.MovieNotFoundException;
import com.ivanfranchin.moviesapi.movie.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoviesController.class)
class MoviesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private MovieService movieService;

    // -- helpers --

    private Movie buildMovie(String imdbId) {
        return Movie.from(new AddMovieRequest(imdbId, "Resident Evil", "Paul W.S. Anderson", 2002));
    }

    // -- GET /api/movies --

    @Test
    public void testGetMoviesReturnsEmptyArrayWhenNoMoviesExist() throws Exception {
        when(movieService.getMovies()).thenReturn(List.of());

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetMoviesReturnsAllMovies() throws Exception {
        Movie movie1 = buildMovie("tt0120804");
        Movie movie2 = buildMovie("tt0119698");
        when(movieService.getMovies()).thenReturn(List.of(movie1, movie2));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].imdbId").value("tt0120804"))
                .andExpect(jsonPath("$[1].imdbId").value("tt0119698"));
    }

    // -- GET /api/movies/{imdbId} --

    @Test
    public void testGetMovieReturnsMovieWhenFound() throws Exception {
        Movie movie = buildMovie("tt0120804");
        when(movieService.validateAndGetMovie("tt0120804")).thenReturn(movie);

        mockMvc.perform(get("/api/movies/tt0120804"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imdbId").value("tt0120804"))
                .andExpect(jsonPath("$.title").value("Resident Evil"))
                .andExpect(jsonPath("$.director").value("Paul W.S. Anderson"))
                .andExpect(jsonPath("$.year").value(2002));
    }

    @Test
    public void testGetMovieReturns404WhenNotFound() throws Exception {
        when(movieService.validateAndGetMovie("tt9999999"))
                .thenThrow(new MovieNotFoundException("tt9999999"));

        mockMvc.perform(get("/api/movies/tt9999999"))
                .andExpect(status().isNotFound());
    }

    // -- POST /api/movies --

    @Test
    public void testAddMovieReturns201WithCreatedMovie() throws Exception {
        AddMovieRequest request = new AddMovieRequest("tt0120804", "Resident Evil", "Paul W.S. Anderson", 2002);
        Movie movie = Movie.from(request);
        when(movieService.saveMovie(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.imdbId").value("tt0120804"))
                .andExpect(jsonPath("$.title").value("Resident Evil"))
                .andExpect(jsonPath("$.director").value("Paul W.S. Anderson"))
                .andExpect(jsonPath("$.year").value(2002));
    }

    @Test
    public void testAddMovieReturns400WhenImdbIdIsBlank() throws Exception {
        AddMovieRequest request = new AddMovieRequest("", "Resident Evil", "Paul W.S. Anderson", 2002);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddMovieReturns400WhenTitleIsBlank() throws Exception {
        AddMovieRequest request = new AddMovieRequest("tt0120804", "", "Paul W.S. Anderson", 2002);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddMovieReturns400WhenDirectorIsBlank() throws Exception {
        AddMovieRequest request = new AddMovieRequest("tt0120804", "Resident Evil", "", 2002);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddMovieReturns400WhenYearIsNegative() throws Exception {
        AddMovieRequest request = new AddMovieRequest("tt0120804", "Resident Evil", "Paul W.S. Anderson", -1);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddMovieReturns400WhenYearIsZero() throws Exception {
        AddMovieRequest request = new AddMovieRequest("tt0120804", "Resident Evil", "Paul W.S. Anderson", 0);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // -- DELETE /api/movies/{imdbId} --

    @Test
    public void testDeleteMovieReturns204() throws Exception {
        Movie movie = buildMovie("tt0120804");
        when(movieService.validateAndGetMovie("tt0120804")).thenReturn(movie);

        mockMvc.perform(delete("/api/movies/tt0120804"))
                .andExpect(status().isNoContent());

        verify(movieService).deleteMovie(movie);
    }

    @Test
    public void testDeleteMovieReturns404WhenNotFound() throws Exception {
        when(movieService.validateAndGetMovie("tt9999999"))
                .thenThrow(new MovieNotFoundException("tt9999999"));

        mockMvc.perform(delete("/api/movies/tt9999999"))
                .andExpect(status().isNotFound());
    }
}

package com.mycompany.moviesapi.rest;

import com.mycompany.moviesapi.mapper.MovieMapper;
import com.mycompany.moviesapi.model.Movie;
import com.mycompany.moviesapi.rest.dto.CreateMovieDto;
import com.mycompany.moviesapi.rest.dto.MovieDto;
import com.mycompany.moviesapi.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @GetMapping
    public List<MovieDto> getMovies() {
        return movieService.getMovies().stream().map(movieMapper::toMovieDto).collect(Collectors.toList());
    }

    @GetMapping("/{imdbId}")
    public MovieDto getMovie(@PathVariable String imdbId) {
        Movie movie = movieService.validateAndGetMovie(imdbId);
        return movieMapper.toMovieDto(movie);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieDto createMovie(@Valid @RequestBody CreateMovieDto createMovieDto) {
        Movie movie = movieMapper.toMovie(createMovieDto);
        movie = movieService.saveMovie(movie);
        return movieMapper.toMovieDto(movie);
    }

    @DeleteMapping("/{imdbId}")
    public MovieDto deleteMovie(@PathVariable String imdbId) {
        Movie movie = movieService.validateAndGetMovie(imdbId);
        movieService.deleteMovie(movie);
        return movieMapper.toMovieDto(movie);
    }

}
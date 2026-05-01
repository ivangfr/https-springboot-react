package com.ivanfranchin.moviesapi.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ivanfranchin.moviesapi.movie.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, String> {}

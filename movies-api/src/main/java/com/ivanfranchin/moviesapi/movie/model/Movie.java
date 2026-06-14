package com.ivanfranchin.moviesapi.movie.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Movie {

  @Id private String imdbId;
  private String title;
  private String director;

  @Column(name = "movie_year")
  private Integer year;
}

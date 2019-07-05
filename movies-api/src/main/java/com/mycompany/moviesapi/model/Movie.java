package com.mycompany.moviesapi.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Movie {

  @Id
  private String imdbId;
  private String title;
  private String director;
  private String year;

}
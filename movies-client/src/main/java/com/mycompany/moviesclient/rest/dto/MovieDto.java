package com.mycompany.moviesclient.rest.dto;

import lombok.Data;

@Data
public class MovieDto {

  private String imdbId;
  private String title;
  private String director;
  private Integer year;

}
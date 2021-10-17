package com.mycompany.moviesapi.rest.dto;

import lombok.Value;

@Value
public class MovieResponse {

    String imdbId;
    String title;
    String director;
    Integer year;
}
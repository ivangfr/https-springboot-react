package com.ivanfranchin.moviesapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
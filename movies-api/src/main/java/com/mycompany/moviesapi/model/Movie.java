package com.mycompany.moviesapi.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Movie {

    @Id
    private String imdbId;
    private String title;
    private String director;
    private String year;

}
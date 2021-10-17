package com.mycompany.moviesshell.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddMovieRequest {

    private String imdbId;
    private String title;
    private String director;
    private Integer year;
}
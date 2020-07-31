package com.mycompany.moviesshell.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddMovieDto {

    private String imdbId;
    private String title;
    private String director;
    private Integer year;

}
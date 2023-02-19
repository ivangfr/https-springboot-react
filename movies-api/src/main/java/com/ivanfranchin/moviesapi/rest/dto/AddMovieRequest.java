package com.ivanfranchin.moviesapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddMovieRequest {

    @Schema(example = "tt0120804")
    @NotBlank
    private String imdbId;

    @Schema(example = "Resident Evil")
    @NotBlank
    private String title;

    @Schema(example = "Paul W.S. Anderson")
    @NotBlank
    private String director;

    @Schema(example = "2002")
    @Positive
    private Integer year;
}
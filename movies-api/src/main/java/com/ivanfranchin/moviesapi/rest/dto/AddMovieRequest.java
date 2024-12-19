package com.ivanfranchin.moviesapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddMovieRequest(
        @Schema(example = "tt0120804") @NotBlank String imdbId,
        @Schema(example = "Resident Evil") @NotBlank String title,
        @Schema(example = "Paul W.S. Anderson") @NotBlank String director,
        @Schema(example = "2002") @Positive Integer year) {
}
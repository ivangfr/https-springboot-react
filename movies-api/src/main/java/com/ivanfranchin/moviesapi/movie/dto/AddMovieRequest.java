package com.ivanfranchin.moviesapi.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddMovieRequest(
    @Schema(example = "tt0120804") @NotBlank String imdbId,
    @Schema(example = "Resident Evil") @NotBlank String title,
    @Schema(example = "Paul W.S. Anderson") @NotBlank String director,
    @Schema(example = "2002") @Positive Integer year) {}

package com.ivanfranchin.moviesapi.rest.dto;

public record MovieResponse(String imdbId, String title, String director, Integer year) {
}
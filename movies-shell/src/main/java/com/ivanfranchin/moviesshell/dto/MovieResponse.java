package com.ivanfranchin.moviesshell.dto;

public record MovieResponse(String imdbId, String title, String director, Integer year) {
}
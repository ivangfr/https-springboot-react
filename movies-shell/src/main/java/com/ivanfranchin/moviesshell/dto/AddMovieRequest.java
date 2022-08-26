package com.ivanfranchin.moviesshell.dto;

public record AddMovieRequest(String imdbId, String title, String director, Integer year) {
}
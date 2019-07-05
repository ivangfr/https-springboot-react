package com.mycompany.moviesclient.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateMovieDto {

  @ApiModelProperty(example = "tt0163651")
  @NotBlank
  private String imdbId;

  @ApiModelProperty(position = 2, example = "American Pie")
  @NotBlank
  private String title;

  @ApiModelProperty(position = 3, example = "Paul Weitz, Chris Weitz")
  @NotBlank
  private String director;

  @ApiModelProperty(position = 4, example = "1999")
  @Positive
  private Integer year;

}
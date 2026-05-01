import React, { useState, useCallback, useEffect } from 'react'
import { Container, Grid, Paper, Typography, Divider } from '@mui/material'
import VideocamIcon from '@mui/icons-material/Videocam'
import MovieForm from './MovieForm'
import MovieTable from './MovieTable'
import { moviesApi } from '../misc/MoviesApi'

const formInitialState = {
  imdbId: '',
  title: '',
  director: '',
  year: '',
  imdbIdError: false,
  titleError: false,
  directorError: false,
  yearError: false
}

function MoviePage() {
  const [movies, setMovies] = useState([])
  const [form, setForm] = useState({ ...formInitialState })

  const handleGetMovies = useCallback(() => {
    moviesApi
      .getMovies()
      .then((response) => {
        setMovies(response.data)
      })
      .catch((error) => {
        console.log(error)
      })
  }, [])

  useEffect(() => {
    handleGetMovies()
  }, [handleGetMovies])

  const handleChange = (e) => {
    const { id, value } = e.target
    setForm((prev) => ({ ...prev, [id]: value }))
  }

  const clearForm = () => {
    setForm({ ...formInitialState })
  }

  const isValidForm = () => {
    const imdbIdError = form.imdbId.trim() === ''
    const titleError = form.title.trim() === ''
    const directorError = form.director.trim() === ''
    const yearValue = parseInt(form.year, 10)
    const yearError =
      isNaN(yearValue) ||
      yearValue < 1888

    setForm((prev) => ({
      ...prev,
      imdbIdError,
      titleError,
      directorError,
      yearError
    }))

    return !(imdbIdError || titleError || directorError || yearError)
  }

  const handleSaveMovie = () => {
    if (!isValidForm()) {
      return
    }

    const { imdbId, title, director, year } = form
    const movie = { imdbId, title, director, year }

    moviesApi
      .saveMovie(movie)
      .then(() => {
        clearForm()
        handleGetMovies()
      })
      .catch((error) => {
        console.log(error)
      })
  }

  const handleDeleteMovie = (id) => {
    moviesApi
      .deleteMovie(id)
      .then(() => {
        handleGetMovies()
      })
      .catch((error) => {
        console.log(error)
      })
  }

  const handleEditMovie = (movie) => {
    setForm({
      imdbId: movie.imdbId,
      title: movie.title,
      director: movie.director,
      year: movie.year.toString(),
      imdbIdError: false,
      titleError: false,
      directorError: false,
      yearError: false
    })
  }

  return (
    <Container sx={{ mt: 3 }}>
      <Grid container spacing={2}>
        <Grid size={{ xs: 12, md: 4 }}>
          <Paper sx={{ p: 2 }}>
            <Typography
              variant='h5'
              sx={{ display: 'flex', alignItems: 'center', gap: 1 }}
            >
              <VideocamIcon />
              Movies
            </Typography>
            <Divider sx={{ my: 1 }} />
            <MovieForm
              form={form}
              handleChange={handleChange}
              handleSaveMovie={handleSaveMovie}
              clearForm={clearForm}
            />
          </Paper>
        </Grid>
        <Grid size={{ xs: 12, md: 8 }}>
          <MovieTable
            movies={movies}
            handleDeleteMovie={handleDeleteMovie}
            handleEditMovie={handleEditMovie}
          />
        </Grid>
      </Grid>
    </Container>
  )
}

export default MoviePage

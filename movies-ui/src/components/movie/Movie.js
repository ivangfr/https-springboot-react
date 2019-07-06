import React, { Component } from 'react'
import { Container, Grid, Segment } from 'semantic-ui-react'
import MovieForm from './MovieForm'
import MovieTable from './MovieTable'
import moviesApi from '../misc/movies-api'

class Movie extends Component {
  formInitialState = {
    imdbId: '',
    title: '',
    director: '',
    year: '',

    imdbIdError: false,
    titleError: false,
    directorError: false,
    yearError: false,
  }

  state = {
    movies: [],
    form: { ...this.formInitialState },
  }

  componentDidMount() {
    this.getAllMovies()
  }

  handleChange = (e) => {
    const { id, value } = e.target
    const form = { ...this.state.form }
    form[id] = value
    this.setState({ form })
  }

  getAllMovies = () => {
    moviesApi.get('movies')
      .then(response => {
        const movies = response.data
        this.setState({ movies })
      })
      .catch(error => {
        console.log(error)
      })
  }

  saveMovie = () => {
    if (!this.isValidForm()) {
      return
    }

    const { imdbId, title, director, year } = this.state.form
    const movie = { imdbId: imdbId, title: title, director: director, year: year }

    moviesApi.post('movies', movie, {
      headers: {
        'Content-type': 'application/json'
      }
    })
      .then(() => {
        this.clearForm()
        this.getAllMovies()
      })
      .catch(error => {
        console.log(error)
      })
  }

  deleteMovie = (id) => {
    moviesApi.delete(`movies/${id}`)
      .then(() => {
        this.getAllMovies()
      })
      .catch(error => {
        console.log(error)
      })
  }

  editMovie = (movie) => {
    const form = {
      imdbId: movie.imdbId,
      title: movie.title,
      director: movie.director,
      year: movie.year,
      imdbIdError: false,
      titleError: false,
      directorError: false,
      yearError: false,
    }
    this.setState({ form })
  }

  clearForm = () => {
    this.setState({
      form: { ...this.formInitialState }
    })
  }

  isValidForm = () => {
    const form = { ...this.state.form }
    const imdbIdError = form.imdbId.trim() === ''
    const titleError = form.title.trim() === ''
    const directorError = form.director.trim() === ''
    const yearError = form.year.trim() === ''

    form.imdbIdError = imdbIdError
    form.titleError = titleError
    form.directorError = directorError
    form.yearError = yearError

    this.setState({ form })
    return (imdbIdError || titleError || directorError || yearError) ? false : true
  }

  render() {
    return (
      <Container>
        <Grid>
          <Grid.Column mobile={16} tablet={16} computer={4}>
            <Segment>
              <MovieForm
                form={this.state.form}
                handleChange={this.handleChange}
                saveMovie={this.saveMovie}
                clearForm={this.clearForm}
              />
            </Segment>
          </Grid.Column>
          <Grid.Column mobile={16} tablet={16} computer={12}>
            <Segment>
              <MovieTable
                movies={this.state.movies}
                deleteMovie={this.deleteMovie}
                editMovie={this.editMovie}
              />
            </Segment>
          </Grid.Column>
        </Grid>
      </Container>
    )
  }
}

export default Movie
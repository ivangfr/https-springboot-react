import React, { Component } from 'react'
import { Container, Grid, Segment, Header, Divider, Icon } from 'semantic-ui-react'
import MovieForm from './MovieForm'
import MovieTable from './MovieTable'
import { moviesApi } from '../misc/MoviesApi'

class MoviePage extends Component {
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
    this.handleGetMovies()
  }

  handleChange = (e) => {
    const { id, value } = e.target
    const form = { ...this.state.form }
    form[id] = value
    this.setState({ form })
  }

  handleGetMovies = () => {
    moviesApi.getMovies()
      .then(response => {
        const movies = response.data
        this.setState({ movies })
      })
      .catch(error => {
        console.log(error)
      })
  }

  handleSaveMovie = () => {
    if (!this.isValidForm()) {
      return
    }

    const { imdbId, title, director, year } = this.state.form
    const movie = { imdbId, title, director, year }

    moviesApi.saveMovie(movie)
      .then(() => {
        this.clearForm()
        this.handleGetMovies()
      })
      .catch(error => {
        console.log(error)
      })
  }

  handleDeleteMovie = (id) => {
    moviesApi.deleteMovie(id)
      .then(() => {
        this.handleGetMovies()
      })
      .catch(error => {
        console.log(error)
      })
  }

  handleEditMovie = (movie) => {
    const form = {
      imdbId: movie.imdbId,
      title: movie.title,
      director: movie.director,
      year: movie.year.toString(),
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
              <Header as='h2'>
                <Icon name='video camera' />
                <Header.Content>Movies</Header.Content>
              </Header>
              <Divider />
              <MovieForm
                form={this.state.form}
                handleChange={this.handleChange}
                handleSaveMovie={this.handleSaveMovie}
                clearForm={this.clearForm}
              />
            </Segment>
          </Grid.Column>
          <Grid.Column mobile={16} tablet={16} computer={12}>
            <MovieTable
              movies={this.state.movies}
              handleDeleteMovie={this.handleDeleteMovie}
              handleEditMovie={this.handleEditMovie}
            />
          </Grid.Column>
        </Grid>
      </Container>
    )
  }
}

export default MoviePage
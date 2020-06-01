import axios from 'axios'

export const moviesApi = {
  getMovies,
  saveMovie,
  deleteMovie
}

function getMovies() {
  return instance.get('/api/movies')
}

function saveMovie(movie) {
  return instance.post('/api/movies', movie, {
    headers: { 'Content-type': 'application/json' }
  })
}

function deleteMovie(id) {
  return instance.delete(`/api/movies/${id}`)
}

// -- Axios

const instance = axios.create({
  baseURL: 'https://localhost:8443'
})
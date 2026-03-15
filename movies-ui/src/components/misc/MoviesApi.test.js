import { vi, describe, it, expect, beforeEach } from 'vitest'

const mockGet = vi.fn()
const mockPost = vi.fn()
const mockDelete = vi.fn()

vi.mock('axios', () => ({
  default: {
    create: () => ({
      get: mockGet,
      post: mockPost,
      delete: mockDelete,
    }),
  },
}))

const { moviesApi } = await import('./MoviesApi')

describe('MoviesApi', () => {
  beforeEach(() => {
    mockGet.mockReset()
    mockPost.mockReset()
    mockDelete.mockReset()
  })

  describe('getMovies', () => {
    it('calls GET /api/movies', () => {
      mockGet.mockResolvedValue({ data: [] })
      moviesApi.getMovies()
      expect(mockGet).toHaveBeenCalledWith('/api/movies')
    })

    it('returns the response', async () => {
      const movies = [{ imdbId: 'tt0111161', title: 'The Shawshank Redemption' }]
      mockGet.mockResolvedValue({ data: movies })
      const response = await moviesApi.getMovies()
      expect(response.data).toEqual(movies)
    })
  })

  describe('saveMovie', () => {
    it('calls POST /api/movies with the movie payload and JSON header', () => {
      const movie = { imdbId: 'tt0111161', title: 'The Shawshank Redemption', director: 'Frank Darabont', year: '1994' }
      mockPost.mockResolvedValue({ data: movie })
      moviesApi.saveMovie(movie)
      expect(mockPost).toHaveBeenCalledWith(
        '/api/movies',
        movie,
        { headers: { 'Content-type': 'application/json' } }
      )
    })
  })

  describe('deleteMovie', () => {
    it('calls DELETE /api/movies/:id', () => {
      mockDelete.mockResolvedValue({})
      moviesApi.deleteMovie('tt0111161')
      expect(mockDelete).toHaveBeenCalledWith('/api/movies/tt0111161')
    })
  })
})

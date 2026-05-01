import React from 'react'
import { render, screen, fireEvent } from '@testing-library/react'
import MovieTable from './MovieTable'

const movies = [
  {
    imdbId: 'tt0111161',
    title: 'The Shawshank Redemption',
    director: 'Frank Darabont',
    year: 1994
  },
  {
    imdbId: 'tt0068646',
    title: 'The Godfather',
    director: 'Francis Ford Coppola',
    year: 1972
  }
]

function renderTable(overrides = {}) {
  const props = {
    movies: movies,
    handleDeleteMovie: vi.fn(),
    handleEditMovie: vi.fn(),
    ...overrides
  }
  return { ...render(<MovieTable {...props} />), props }
}

describe('MovieTable', () => {
  it('renders column headers', () => {
    renderTable()
    expect(screen.getByText('ImdbID')).toBeInTheDocument()
    expect(screen.getByText('Title')).toBeInTheDocument()
    expect(screen.getByText('Director')).toBeInTheDocument()
    expect(screen.getByText('Year')).toBeInTheDocument()
  })

  it('renders a row for each movie', () => {
    renderTable()
    expect(screen.getByText('tt0111161')).toBeInTheDocument()
    expect(screen.getByText('The Shawshank Redemption')).toBeInTheDocument()
    expect(screen.getByText('Frank Darabont')).toBeInTheDocument()
    expect(screen.getByText('1994')).toBeInTheDocument()

    expect(screen.getByText('tt0068646')).toBeInTheDocument()
    expect(screen.getByText('The Godfather')).toBeInTheDocument()
    expect(screen.getByText('Francis Ford Coppola')).toBeInTheDocument()
    expect(screen.getByText('1972')).toBeInTheDocument()
  })

  it('shows empty state when movies list is empty', () => {
    renderTable({ movies: [] })
    expect(screen.getByText(/no movies found/i)).toBeInTheDocument()
  })

  it('shows empty state when movies is null', () => {
    renderTable({ movies: null })
    expect(screen.getByText(/no movies found/i)).toBeInTheDocument()
  })

  it('calls handleDeleteMovie with the correct imdbId when delete is clicked', () => {
    const { props } = renderTable()
    const deleteButtons = screen.getAllByRole('button', { name: '' })
    // first button in first row is delete
    fireEvent.click(deleteButtons[0])
    expect(props.handleDeleteMovie).toHaveBeenCalledWith('tt0111161')
  })

  it('calls handleEditMovie with the correct movie when edit is clicked', () => {
    const { props } = renderTable()
    const editButtons = screen.getAllByRole('button', { name: '' })
    // second button in first row is edit
    fireEvent.click(editButtons[1])
    expect(props.handleEditMovie).toHaveBeenCalledWith(movies[0])
  })
})

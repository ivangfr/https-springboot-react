import React from 'react'
import { render, screen, fireEvent } from '@testing-library/react'
import MovieForm from './MovieForm'

const baseForm = {
  imdbId: '',
  title: '',
  director: '',
  year: '',
  imdbIdError: false,
  titleError: false,
  directorError: false,
  yearError: false
}

function renderForm(overrides = {}, handlers = {}) {
  const props = {
    form: { ...baseForm, ...overrides },
    handleChange: handlers.handleChange ?? vi.fn(),
    handleSaveMovie: handlers.handleSaveMovie ?? vi.fn(),
    clearForm: handlers.clearForm ?? vi.fn()
  }
  return render(<MovieForm {...props} />)
}

describe('MovieForm', () => {
  it('renders all fields and buttons', () => {
    renderForm()
    expect(screen.getByLabelText(/imdbid/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/title/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/director/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/year/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /save/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /cancel/i })).toBeInTheDocument()
  })

  it('renders field values from form prop', () => {
    renderForm({
      imdbId: 'tt1234',
      title: 'Inception',
      director: 'Nolan',
      year: '2010'
    })
    expect(screen.getByLabelText(/imdbid/i)).toHaveValue('tt1234')
    expect(screen.getByLabelText(/title/i)).toHaveValue('Inception')
    expect(screen.getByLabelText(/director/i)).toHaveValue('Nolan')
    expect(screen.getByLabelText(/year/i)).toHaveValue(2010)
  })

  it('calls handleChange when a field is changed', () => {
    const handleChange = vi.fn()
    renderForm({}, { handleChange })
    fireEvent.change(screen.getByLabelText(/title/i), {
      target: { value: 'Dune' }
    })
    expect(handleChange).toHaveBeenCalledTimes(1)
  })

  it('calls handleSaveMovie when form is submitted', () => {
    const handleSaveMovie = vi.fn()
    renderForm({}, { handleSaveMovie })
    fireEvent.click(screen.getByRole('button', { name: /save/i }))
    expect(handleSaveMovie).toHaveBeenCalledTimes(1)
  })

  it('calls clearForm when Cancel is clicked', () => {
    const clearForm = vi.fn()
    renderForm({}, { clearForm })
    fireEvent.click(screen.getByRole('button', { name: /cancel/i }))
    expect(clearForm).toHaveBeenCalledTimes(1)
  })

  it('shows error state on fields when error props are true', () => {
    renderForm({
      imdbIdError: true,
      titleError: true,
      directorError: true,
      yearError: true
    })
    // MUI sets aria-invalid on inputs when error=true
    expect(screen.getByLabelText(/imdbid/i)).toHaveAttribute(
      'aria-invalid',
      'true'
    )
    expect(screen.getByLabelText(/title/i)).toHaveAttribute(
      'aria-invalid',
      'true'
    )
    expect(screen.getByLabelText(/director/i)).toHaveAttribute(
      'aria-invalid',
      'true'
    )
    expect(screen.getByLabelText(/year/i)).toHaveAttribute(
      'aria-invalid',
      'true'
    )
  })

  it('shows year helper text when yearError is true', () => {
    renderForm({ yearError: true })
    expect(screen.getByText(/enter a year after 1888/i)).toBeInTheDocument()
  })

  it('does not show year helper text when yearError is false', () => {
    renderForm({ yearError: false })
    expect(
      screen.queryByText(/enter a year after 1888/i)
    ).not.toBeInTheDocument()
  })
})

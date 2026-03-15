import React from 'react'
import { TextField, Button, Stack } from '@mui/material'

function MovieForm({ form, handleChange, handleSaveMovie, clearForm }) {
  return (
    <Stack spacing={2} component='form' noValidate onSubmit={(e) => { e.preventDefault(); handleSaveMovie() }}>
      <TextField
        fullWidth
        size='small'
        label='ImdbID'
        id='imdbId'
        onChange={handleChange}
        value={form.imdbId}
        error={form.imdbIdError}
      />
      <TextField
        fullWidth
        size='small'
        label='Title'
        id='title'
        onChange={handleChange}
        value={form.title}
        error={form.titleError}
      />
      <TextField
        fullWidth
        size='small'
        label='Director'
        id='director'
        onChange={handleChange}
        value={form.director}
        error={form.directorError}
      />
      <TextField
        fullWidth
        size='small'
        label='Year'
        id='year'
        type='number'
        onChange={handleChange}
        value={form.year}
        error={form.yearError}
        helperText={form.yearError ? `Enter a year between 1888 and ${new Date().getFullYear()}` : ''}
      />
      <Stack direction='row' spacing={1}>
        <Button fullWidth type='submit' variant='contained' color='primary'>
          Save
        </Button>
        <Button fullWidth type='button' variant='outlined' onClick={clearForm}>
          Cancel
        </Button>
      </Stack>
    </Stack>
  )
}

export default MovieForm

import React from 'react'
import { Form, Button } from 'semantic-ui-react'

function MovieForm({ form, handleChange, handleSaveMovie, clearForm }) {
  return (
    <Form>
      <Form.Input
        fluid
        label='ImdbID'
        id='imdbId'
        onChange={handleChange}
        value={form.imdbId}
        error={form.imdbIdError}
      />
      <Form.Input
        fluid
        label='Title'
        id='title'
        onChange={handleChange}
        value={form.title}
        error={form.titleError}
      />
      <Form.Input
        fluid
        label='Director'
        id='director'
        onChange={handleChange}
        value={form.director}
        error={form.directorError}
      />
      <Form.Input
        fluid
        label='Year'
        id='year'
        type='number'
        onChange={handleChange}
        value={form.year}
        error={form.yearError}
      />
      <Button.Group fluid>
        <Button positive onClick={handleSaveMovie}>Save</Button>
        <Button.Or />
        <Button onClick={clearForm}>Cancel</Button>
      </Button.Group>
    </Form>
  )
}

export default MovieForm
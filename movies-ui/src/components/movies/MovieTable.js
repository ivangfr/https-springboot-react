import React from 'react'
import { Table, Button } from 'semantic-ui-react'

function MovieTable({ movies, handleDeleteMovie, handleEditMovie }) {
  const movieList = movies && movies.map(movie => {
    return (
      <Table.Row key={movie.imdbId}>
        <Table.Cell collapsing>
          <Button
            circular
            color='red'
            size='tiny'
            icon='trash'
            onClick={() => handleDeleteMovie(movie.imdbId)}
          />
          <Button
            circular
            color='orange'
            size='tiny'
            icon='edit'
            onClick={() => handleEditMovie(movie)}
          />
        </Table.Cell>
        <Table.Cell>{movie.imdbId}</Table.Cell>
        <Table.Cell>{movie.title}</Table.Cell>
        <Table.Cell>{movie.director}</Table.Cell>
        <Table.Cell>{movie.year}</Table.Cell>
      </Table.Row>
    )
  })

  return (
    <Table compact striped unstackable>
      <Table.Header>
        <Table.Row>
          <Table.HeaderCell width={2} />
          <Table.HeaderCell width={3}>ImdbID</Table.HeaderCell>
          <Table.HeaderCell width={6}>Title</Table.HeaderCell>
          <Table.HeaderCell width={4}>Director</Table.HeaderCell>
          <Table.HeaderCell width={1}>Year</Table.HeaderCell>
        </Table.Row>
      </Table.Header>
      <Table.Body>
        {movieList}
      </Table.Body>
    </Table>
  )
}

export default MovieTable
import React from 'react'
import {
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  IconButton,
} from '@mui/material'
import DeleteIcon from '@mui/icons-material/Delete'
import EditIcon from '@mui/icons-material/Edit'

function MovieTable({ movies, handleDeleteMovie, handleEditMovie }) {
  const hasMovies = movies && movies.length > 0

  const movieList = hasMovies && movies.map(movie => {
    return (
      <TableRow
        key={movie.imdbId}
        sx={{ '&:nth-of-type(odd)': { backgroundColor: 'action.hover' } }}
      >
        <TableCell padding='none' sx={{ whiteSpace: 'nowrap', pl: 1 }}>
          <IconButton size='small' color='error' onClick={() => handleDeleteMovie(movie.imdbId)}>
            <DeleteIcon fontSize='small' />
          </IconButton>
          <IconButton size='small' color='warning' onClick={() => handleEditMovie(movie)}>
            <EditIcon fontSize='small' />
          </IconButton>
        </TableCell>
        <TableCell>{movie.imdbId}</TableCell>
        <TableCell>{movie.title}</TableCell>
        <TableCell>{movie.director}</TableCell>
        <TableCell>{movie.year}</TableCell>
      </TableRow>
    )
  })

  return (
    <Table size='small'>
      <TableHead>
        <TableRow>
          <TableCell width={90} />
          <TableCell>ImdbID</TableCell>
          <TableCell>Title</TableCell>
          <TableCell>Director</TableCell>
          <TableCell>Year</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {movieList}
        {!hasMovies && (
          <TableRow>
            <TableCell colSpan={5} align='center' sx={{ color: 'text.secondary', py: 3 }}>
              No movies found
            </TableCell>
          </TableRow>
        )}
      </TableBody>
    </Table>
  )
}

export default MovieTable

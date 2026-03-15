import React from 'react'
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material'
import Navbar from './components/misc/Navbar'
import MoviePage from './components/movies/MoviePage'

const theme = createTheme()

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Navbar />
      <MoviePage />
    </ThemeProvider>
  )
}

export default App

import axios from 'axios'

export default axios.create({
  baseURL: 'https://localhost:8443/api/'
})
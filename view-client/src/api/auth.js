import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  withCredentials: true
});

export const login = async (credentials) => {
  try {
    const response = await api.post('/api/login', credentials);
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

// Другие API вызовы...
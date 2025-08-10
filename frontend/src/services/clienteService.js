import api from './api';

// Cliente service for managing customer data
export const clienteService = {
  // Get all clients with pagination
  getAll: async (page = 0, size = 20) => {
    const response = await api.get(`/clientes?page=${page}&size=${size}`);
    return response.data;
  },

  // Get client by ID
  getById: async (id) => {
    const response = await api.get(`/clientes/${id}`);
    return response.data;
  },

  // Search clients by name
  search: async (nome, page = 0, size = 20) => {
    const response = await api.get(`/clientes/search?nome=${nome}&page=${page}&size=${size}`);
    return response.data;
  },

  // Create new client
  create: async (clienteData) => {
    const response = await api.post('/clientes', clienteData);
    return response.data;
  },

  // Update client
  update: async (id, clienteData) => {
    const response = await api.put(`/clientes/${id}`, clienteData);
    return response.data;
  },

  // Delete client
  delete: async (id) => {
    await api.delete(`/clientes/${id}`);
  }
};

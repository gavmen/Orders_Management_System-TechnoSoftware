import api from './api';

// Pedido service for managing order data
export const pedidoService = {
  // Get all orders with pagination
  getAll: async (page = 0, size = 20) => {
    const response = await api.get(`/pedidos?page=${page}&size=${size}`);
    return response.data;
  },

  // Get order by ID
  getById: async (id) => {
    const response = await api.get(`/pedidos/${id}`);
    return response.data;
  },

  // Get orders by client ID
  getByCliente: async (clienteId, page = 0, size = 20) => {
    const response = await api.get(`/pedidos/cliente/${clienteId}?page=${page}&size=${size}`);
    return response.data;
  },

  // Get orders by status
  getByStatus: async (status, page = 0, size = 20) => {
    const response = await api.get(`/pedidos/status/${status}?page=${page}&size=${size}`);
    return response.data;
  },

  // Create new order (main business logic function)
  create: async (pedidoData) => {
    const response = await api.post('/pedidos', pedidoData);
    return response.data;
  },

  // Get client's total orders in last 30 days
  getClienteTotal30Dias: async (clienteId) => {
    const response = await api.get(`/pedidos/cliente/${clienteId}/total`);
    return response.data;
  },

  // Filter orders by date range
  filterByDate: async (dataInicio, dataFim, page = 0, size = 20) => {
    const response = await api.get(`/pedidos/periodo?inicio=${dataInicio}&fim=${dataFim}&page=${page}&size=${size}`);
    return response.data;
  }
};

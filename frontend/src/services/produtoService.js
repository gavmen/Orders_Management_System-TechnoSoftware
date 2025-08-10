import api from './api';

// Produto service for managing product data
export const produtoService = {
  // Get all products with pagination
  getAll: async (page = 0, size = 20) => {
    const response = await api.get(`/produtos?page=${page}&size=${size}`);
    return response.data;
  },

  // Get product by ID
  getById: async (id) => {
    const response = await api.get(`/produtos/${id}`);
    return response.data;
  },

  // Search products by name
  search: async (nome, page = 0, size = 20) => {
    const response = await api.get(`/produtos/search?nome=${nome}&page=${page}&size=${size}`);
    return response.data;
  },

  // Filter products by price range
  filterByPrice: async (precoMin, precoMax, page = 0, size = 20) => {
    const response = await api.get(`/produtos/preco?min=${precoMin}&max=${precoMax}&page=${page}&size=${size}`);
    return response.data;
  },

  // Create new product
  create: async (produtoData) => {
    const response = await api.post('/produtos', produtoData);
    return response.data;
  },

  // Update product
  update: async (id, produtoData) => {
    const response = await api.put(`/produtos/${id}`, produtoData);
    return response.data;
  },

  // Delete product
  delete: async (id) => {
    await api.delete(`/produtos/${id}`);
  }
};

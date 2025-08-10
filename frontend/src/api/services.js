import api from './api';

// Customer API services using the configured Axios client
export const customerService = {
  // Get all customers
  getAll: () => api.get('/api/clientes'),
  
  // Get customer by ID
  getById: (id) => api.get(`/api/clientes/${id}`),
  
  // Get customer credit balance information
  getCreditBalance: (id) => api.get(`/api/clientes/${id}/credito`),
  
  // Create new customer
  create: (customerData) => api.post('/api/clientes', customerData),
  
  // Update existing customer
  update: (id, customerData) => api.put(`/api/clientes/${id}`, customerData),
  
  // Delete customer
  delete: (id) => api.delete(`/api/clientes/${id}`)
};

// Product API services
export const productService = {
  // Get all products
  getAll: () => api.get('/api/produtos'),
  
  // Get product by ID
  getById: (id) => api.get(`/api/produtos/${id}`),
  
  // Create new product
  create: (productData) => api.post('/api/produtos', productData),
  
  // Update existing product
  update: (id, productData) => api.put(`/api/produtos/${id}`, productData),
  
  // Delete product
  delete: (id) => api.delete(`/api/produtos/${id}`)
};

// Order API services
export const orderService = {
  // Get all orders
  getAll: () => api.get('/api/pedidos'),
  
  // Get order by ID
  getById: (id) => api.get(`/api/pedidos/${id}`),
  
  // Create new order (with credit limit validation)
  create: (orderData) => api.post('/api/pedidos', orderData),
  
  // Update existing order
  update: (id, orderData) => api.put(`/api/pedidos/${id}`, orderData),
  
  // Delete order
  delete: (id) => api.delete(`/api/pedidos/${id}`)
};
